package lans.hotels.datasource.mappers;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
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
    protected String insertStatement() {
        return "INSERT INTO " +
                " room_booking rb " +
                " VALUES " +
                " (DEFAULT, b.id, ?, TRUE, ?, ?) " + // (int), int: booking_id, int: room_id, (bool), string: guestName, int: numGuests
                " RETURNING * ";
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Integer insert(DomainObject domainObject) throws Exception {
        return -1;
    }

    @Override
    public RoomBooking update(AbstractDomainObject domainObject) {
        RoomBooking roomBooking = (RoomBooking) domainObject;
        String updateStatement = "UPDATE room_booking SET is_active = " + roomBooking.getActive()  +  ", no_of_guests = " + roomBooking.getNumOfGuests() + " WHERE id = " + roomBooking.getId() + ";";

        System.out.println("UPDATE ROOM BOOKING QUERY : ");
        System.out.println(updateStatement);

        try (PreparedStatement statement = connection.prepareStatement(updateStatement)) {
            statement.executeUpdate();
            System.out.println("RoomBookingMapper : Booking with id " + roomBooking.getId() + " updated in DataMapper...");

            BookingsSearchCriteria criteria = new BookingsSearchCriteria();
            criteria.setBookingId(roomBooking.getId());
            return null;
        } catch (Exception e) {
            System.out.println("Exception occurred at RoomBookingDataMapper:update() execution");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public ArrayList<RoomBooking> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception {
        RoomBookingSearchCriteria roomBookingSearchCriteria = (RoomBookingSearchCriteria) criteria;
        String findByCriteriaStatement = "SELECT r.id as id,h.name as hotel_name, start_date, end_date, m.id as room_id, r.is_active, no_of_guests,main_guest\n" +
                "FROM booking b\n" +
                "         JOIN ( room_booking r\n" +
                "    JOIN room m\n" +
                "    ON r.room_id=m.id)\n" +
                "              ON b.id = r.booking_id\n" +
                "         JOIN hotel h ON h.id = b.hotel_id\n" +
                "         JOIN app_user u ON u.id=b.customer_id\n";

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
    protected RoomBooking doLoad(Integer id, ResultSet resultSet) throws Exception {
        RoomBooking booking = new RoomBooking(id, dataSource,  resultSet.getInt("room_id"), resultSet.getBoolean("is_active"), resultSet.getString("main_guest"), resultSet.getInt("no_of_guests"));
        return booking;
    }

    @Override
    public ArrayList<RoomBooking> findAll() throws SQLException {
        return null;
    }
}
