package lans.hotels.datasource.mappers;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.user_types.Customer;
import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.DateRange;
import lans.hotels.domain.utils.District;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookingDataMapper extends AbstractPostgresDataMapper<Booking> {
    protected BookingDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "booking", dataSource);
        idPrefix = "b.";
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Boolean create(DomainObject domainObject) {
        // TODO: TBR - Just for testing
        return null;
    }

    @Override
    public Booking update(AbstractDomainObject domainObject) {
        Booking booking = (Booking) domainObject;
        String updateStatement = "UPDATE booking SET is_active = " + booking.getActive()  +  " WHERE id = " + booking.getId() + ";";

        System.out.println("UPDATE BOOKING QUERY : ");
        System.out.println(updateStatement);

        try (PreparedStatement statement = connection.prepareStatement(updateStatement)) {
            statement.executeQuery();
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
    public ArrayList<Booking> insert() throws Exception {
        return null;
    }

    @Override
    public ArrayList<Booking> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception {
        BookingsSearchCriteria bookingsSearchCriteria = (BookingsSearchCriteria) criteria;
        String findByCriteriaStatement = "SELECT b.id as id, b.is_active as is_active, h.id as hotel_id,h.name as hotel_name," +
                "u.name as user_name,c.id as customer_id, start_date, end_date," +
                " s.type as room_type, m.id as room_id,no_of_guests,main_guest, hg.id as hotel_group_id\n" +
                "    FROM booking b\n" +
                "        JOIN ( room_booking r\n" +
                "            JOIN (room m\n" +
                "                JOIN room_spec s\n" +
                "                ON m.room_spec_id=s.id)\n" +
                "            ON r.room_id=m.id)\n" +
                "        ON b.id = r.booking_id\n" +
                "        JOIN hotel h ON h.id = b.hotel_id\n" +
                "        JOIN customer c ON c.id=b.customer_id\n" +
                "        JOIN app_user u on u.id = c.user_id " +
                "        JOIN hotel_group hg on h.hotel_group_id = hg.id ";


        if(bookingsSearchCriteria.getBookingId()!=null) {
            findByCriteriaStatement += "WHERE  b.id = '" + bookingsSearchCriteria.getBookingId() + "'";
        }

        if(bookingsSearchCriteria.getCustomerId()!=null) {
            findByCriteriaStatement += "WHERE b.customer_id = '" + bookingsSearchCriteria.getCustomerId() + "'";
        }
        if(bookingsSearchCriteria.getHotelGroupId()!=null) {
            findByCriteriaStatement += "WHERE hotel_group_id = '" + bookingsSearchCriteria.getHotelGroupId() + "'";
        }
        System.out.println("Booking query \n"+findByCriteriaStatement);
        try (PreparedStatement statement = connection.prepareStatement(findByCriteriaStatement)) {
            ResultSet resultSet = statement.executeQuery();
            Booking loadedBooking = load(resultSet);
            while (loadedBooking != null) {
                loadedBooking = load(resultSet);
            }
            System.out.println("BookingMapper : All Bookings loaded...");
            return new ArrayList<>(loadedMap.values());
        } catch (Exception e) {
            System.out.println("Exception occurred at findBySearchCriteria");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected String findStatement() {
        return null;
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
