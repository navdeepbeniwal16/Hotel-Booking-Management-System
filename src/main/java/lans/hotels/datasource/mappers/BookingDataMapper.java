package lans.hotels.datasource.mappers;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.utils.DateRange;

import java.sql.*;
import java.util.ArrayList;

public class BookingDataMapper extends AbstractPostgresDataMapper<Booking> {
    protected BookingDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "booking", dataSource);
        idPrefix = "b.";
    }

    @Override
    protected String findStatement() {
        String statement =
                "SELECT b.id as id, b.hotel_id as hotel_id, b.customer_id, " +
                        "b.start_date, b.end_date, b.is_active as is_active, b.version, " +
                        "h.hotel_group_id, " +
                        "h.name as hotel_name, " +
                        "u.name as customer_name " +
                        "FROM booking b " +
                        "JOIN hotel h ON h.id = b.hotel_id " +
                        "JOIN app_user u on u.id = b.customer_id ";
        return statement;
    }

    @Override
    protected String insertStatement() {
        return "INSERT INTO " + " booking " +
                " VALUES " +
                " (DEFAULT, ?, ?, ?, ?, TRUE) " +
                " RETURNING *;";
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Integer insert(DomainObject domainObject) throws SQLException {
        Booking booking = (Booking) domainObject;
        String sql = "WITH new_booking AS ( " +
                         " INSERT INTO booking (hotel_id, customer_id, start_date, end_date, is_active) " +
                         " VALUES (" + booking.getHotelId() + ", " + booking.getCustomerId() +
                         ", " + "'" + booking.getDateRange().getFrom().toString() + "'" +
                         ", " + "'" + booking.getDateRange().getTo().toString() + "'" +
                         ", " + true + ") " +
                         " RETURNING id " +
                     " ) INSERT INTO room_booking (booking_id, room_id, is_active, main_guest, no_of_guests) " +
                     " VALUES ";

        for(RoomBooking roomBooking: booking.getRoomBookings().values()) {
            sql = sql +
                    " ( (SELECT id FROM new_booking), " + roomBooking.getRoomId() + ", " + true + ", "
                    + "'" + roomBooking.getMainGuest() + "'" +
                    ", " + roomBooking.getNumOfGuests()  + " ), ";
        }
        sql = sql.substring(0, sql.length() - 2);
        sql = sql + " RETURNING * ; ";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next() ? resultSet.getInt("id") : -1;
    }

    @Override
    public Booking update(AbstractDomainObject domainObject) throws Exception {
        Booking booking = (Booking) domainObject;

        PreparedStatement updateStatement = connection.prepareStatement(
                "UPDATE booking SET version = ?, is_active = ?, start_date = ?, end_date = ? " +
                        "WHERE id = ? AND version = ? ");

        Integer version = booking.getVersion();
        int new_version = version + 1;
        updateStatement.setInt(1,new_version);
        updateStatement.setBoolean(2, booking.getActive());
        updateStatement.setDate(3, booking.getDateRange().getFrom());
        updateStatement.setDate(4, booking.getDateRange().getTo());
        updateStatement.setInt(5,booking.getId());
        updateStatement.setInt(6,version);

        int row_count = updateStatement.executeUpdate();

        BookingsSearchCriteria criteria = new BookingsSearchCriteria();
        criteria.setBookingId(booking.getId());

        ArrayList<Booking> bookings = findBySearchCriteria(criteria);
        if(bookings.size() > 0)
            return bookings.get(0);
        else
            return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public ArrayList<Booking> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception {
        BookingsSearchCriteria bookingsSearchCriteria = (BookingsSearchCriteria) criteria;

        String findBy = findStatement() + "WHERE ";

        PreparedStatement statement = null;
        if(bookingsSearchCriteria.getBookingId() != null) {
            statement = connection.prepareStatement(findBy + "b.id = ?");
            statement.setInt(1,bookingsSearchCriteria.getBookingId());
        } else if (bookingsSearchCriteria.getCustomerId() != null){
            statement = connection.prepareStatement(findBy +"b.customer_id = ?");
            statement.setInt(1,bookingsSearchCriteria.getCustomerId());
        }
        else if (bookingsSearchCriteria.getCustomerEmail() != null){
            statement = connection.prepareStatement(findBy +"u.email = ?");
            statement.setString(1,bookingsSearchCriteria.getCustomerEmail());
        }
        else if (bookingsSearchCriteria.getHotelGroupId() != null){
            statement = connection.prepareStatement(findBy + "hotel_group_id = ?");
            statement.setInt(1,bookingsSearchCriteria.getHotelGroupId());
        } else if (bookingsSearchCriteria.getHotelId() != null){
            statement = connection.prepareStatement(findBy + "hotel_id = ?");
            statement.setInt(1,bookingsSearchCriteria.getHotelId());
        }

        if(statement != null && bookingsSearchCriteria.getStartDate() != null && bookingsSearchCriteria.getEndDate() !=  null) {
            statement = connection.prepareStatement(statement + " AND " + "(start_date >= ? AND start_date <= ?) OR (end_date >= ? AND end_date <= ?) OR (start_date <= ? AND end_date >= ?)");
            statement.setDate(1, new java.sql.Date(bookingsSearchCriteria.getStartDate().getTime()));
            statement.setDate(2, new java.sql.Date(bookingsSearchCriteria.getEndDate().getTime()));
            statement.setDate(3, new java.sql.Date(bookingsSearchCriteria.getStartDate().getTime()));
            statement.setDate(4, new java.sql.Date(bookingsSearchCriteria.getEndDate().getTime()));
            statement.setDate(5, new java.sql.Date(bookingsSearchCriteria.getStartDate().getTime()));
            statement.setDate(6, new java.sql.Date(bookingsSearchCriteria.getEndDate().getTime()));
        }


        ResultSet resultSet = statement.executeQuery();
        Booking bookings = load(resultSet);
        while (bookings != null) {
            bookings = load(resultSet);
        }
        return new ArrayList<>(loadedMap.values());
    }



    @Override
    protected Booking doLoad(Integer id, ResultSet resultSet) throws Exception {
        DateRange dateRange = new DateRange(resultSet.getDate("start_date"),
                resultSet.getDate("end_date"));
        Booking booking =
                new Booking(id,
                        dataSource,
                        resultSet.getInt("hotel_id"),
                        resultSet.getInt("customer_id"),
                        dateRange,
                        resultSet.getBoolean("is_active"),
                        resultSet.getString("hotel_name"),
                        resultSet.getString("customer_name"),
                        resultSet.getInt("version")
                        );
        return booking;
    }

    @Override
    public ArrayList<Booking> findAll() throws SQLException {
        return null;
    }
}
