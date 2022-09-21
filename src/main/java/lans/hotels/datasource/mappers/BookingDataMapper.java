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
        return null;
    }

    @Override
    public <DomainObject extends AbstractDomainObject> DomainObject update(AbstractDomainObject domainObject) {
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
        String findByCriteriaStatement = "SELECT b.id as id,h.id as hotel_id,h.name as hotel_name," +
                "u.name as user_name,c.id as customer_id, start_date, end_date," +
                " s.type as room_type, m.id as room_id,no_of_guests,main_guest\n" +
                "    FROM booking b\n" +
                "        JOIN ( room_booking r\n" +
                "            JOIN (room m\n" +
                "                JOIN room_spec s\n" +
                "                ON m.room_spec_id=s.id)\n" +
                "            ON r.room_id=m.id)\n" +
                "        ON b.id = r.booking_id\n" +
                "        JOIN hotel h ON h.id = b.hotel_id\n" +
                "        JOIN customer c ON c.id=b.customer_id\n" +
                "        JOIN app_user u on u.id = c.user_id ";


        if(bookingsSearchCriteria.getCustomerId()!=null) {
            findByCriteriaStatement += "WHERE b.customer_id = '" + bookingsSearchCriteria.getCustomerId() + "'";
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
                        resultSet.getInt("customer_id"), dateRange);
        return booking;
    }

    @Override
    public ArrayList<Booking> findAll() throws SQLException {
        return null;
    }
}
