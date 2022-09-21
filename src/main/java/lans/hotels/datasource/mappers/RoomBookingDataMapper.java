package lans.hotels.datasource.mappers;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.RoomBookingSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.RoomBooking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoomBookingDataMapper extends AbstractPostgresDataMapper<RoomBooking> {

    protected RoomBookingDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "room_booking", dataSource);
        idPrefix = "rb.";
    }


    @Override
    public <DomainObject extends AbstractDomainObject> Boolean create(DomainObject domainObject) throws Exception {
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
    public ArrayList<RoomBooking> insert() throws Exception {
        return null;
    }

    @Override
    public ArrayList<RoomBooking> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception {
        RoomBookingSearchCriteria roomBookingSearchCriteria = (RoomBookingSearchCriteria) criteria;
        String findByCriteriaStatement = "SELECT b.id as id,h.name as hotel_name, start_date, end_date, s.type as room_type, m.id as room_id, r.is_active, no_of_guests,main_guest\n" +
                "FROM booking b\n" +
                "         JOIN ( room_booking r\n" +
                "    JOIN (room m\n" +
                "        JOIN room_spec s\n" +
                "        ON m.room_spec_id=s.id)\n" +
                "    ON r.room_id=m.id)\n" +
                "              ON b.id = r.booking_id\n" +
                "         JOIN hotel h ON h.id = b.hotel_id\n" +
                "         JOIN customer c ON c.id=b.customer_id\n";

        if(roomBookingSearchCriteria.getBookingId()!=null) {
            findByCriteriaStatement += "WHERE b.id ='" + roomBookingSearchCriteria.getBookingId() + "';";
        }

        System.out.println("Room Booking Search Query");
        System.out.println(findByCriteriaStatement);

        try (PreparedStatement statement = connection.prepareStatement(findByCriteriaStatement)) {
            ResultSet resultSet = statement.executeQuery();
            RoomBooking loadedRoomBooking = load(resultSet);
            while (loadedRoomBooking != null) {
                loadedRoomBooking = load(resultSet);
            }
            System.out.println("RoomBookingMapper : All Room Bookings loaded...");
            return new ArrayList<>(loadedMap.values());
        } catch (Exception e) {
            System.out.println("Exception occurred at findBySearchCriteria");
            e.printStackTrace();
        }

        return new ArrayList<>();
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
    protected RoomBooking doLoad(Integer id, ResultSet resultSet) throws Exception {
        RoomBooking booking = new RoomBooking(id, dataSource,  resultSet.getInt("room_id"), resultSet.getBoolean("is_active"), resultSet.getString("main_guest"), resultSet.getInt("no_of_guests"));
        return booking;
    }

    @Override
    public ArrayList<RoomBooking> findAll() throws SQLException {
        return null;
    }
}
