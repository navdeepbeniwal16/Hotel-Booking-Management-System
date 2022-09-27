package lans.hotels.datasource.mappers;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
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
                "SELECT b.id as id, b.hotel_id as hotel_id, customer_id, hotel_group_id, " +
                        "start_date, end_date, b.is_active as is_active, " +
                        "h.name as hotel_name, " +
                        "u.name as user_name " +
                        "FROM booking b " +
                        "JOIN hotel h ON h.id = b.hotel_id " +
                        "JOIN app_user u on u.id = b.customer_id ";
        System.out.println("BookingDataMapper.findStatement(): " + statement);
        return statement;
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Boolean insert(DomainObject domainObject) {
        return null;
    }

    @Override
    public Booking update(AbstractDomainObject domainObject) {
        Booking booking = (Booking) domainObject;
        String updateStatement = "UPDATE booking SET is_active = " + booking.getActive()  +  " WHERE id = " + booking.getId() + ";";

        System.out.println("UPDATE BOOKING QUERY : ");
        System.out.println(updateStatement);

        try (PreparedStatement statement = connection.prepareStatement(updateStatement)) {
            statement.executeUpdate();
//            statement.executeQuery();
//            connection.commit();
            System.out.println("BookingMapper : Booking with id " + booking.getId() + " updated in DataMapper...");

            BookingsSearchCriteria criteria = new BookingsSearchCriteria();
            criteria.setBookingId(booking.getId());

            ArrayList<Booking> bookings = findBySearchCriteria(criteria);
            if(bookings.size() > 0) return bookings.get(0);
            else return null;
        } catch (Exception e) {
            System.out.println("Exception occurred at BookingDataMapper:update() execution");
            e.printStackTrace();
        }

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
        if (bookingsSearchCriteria.getCustomerId() != null){
            statement = connection.prepareStatement(findBy +"b.customer_id = ?");
            statement.setInt(1,bookingsSearchCriteria.getCustomerId());
        }
        else if (bookingsSearchCriteria.getHotelGroupId() != null){
            statement = connection.prepareStatement(findBy + "hotel_group_id = ?");
            statement.setInt(1,bookingsSearchCriteria.getHotelGroupId());
        }

        System.out.println("Query : "+statement.toString());

        ResultSet resultSet = statement.executeQuery();
        Booking bookings = load(resultSet);
        while (bookings != null) {
            bookings = load(resultSet);
        }
        return new ArrayList<>(loadedMap.values());
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    protected Booking doLoad(Integer id, ResultSet resultSet) throws Exception {
        DateRange dateRange = new DateRange(resultSet.getDate("start_date"),
                resultSet.getDate("end_date"));
        Booking booking =
                new Booking(id,dataSource, resultSet.getInt("hotel_id"),
                        resultSet.getInt("customer_id"), dateRange, resultSet.getBoolean("is_active"));
        return booking;
    }

    @Override
    public ArrayList<Booking> findAll() throws SQLException {
        return null;
    }
}
