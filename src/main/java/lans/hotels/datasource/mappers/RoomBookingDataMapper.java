package lans.hotels.datasource.mappers;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.datasource.search_criteria.RoomBookingSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.RoomBooking;
import lans.hotels.domain.hotel.Hotel;

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
    public RoomBooking update(AbstractDomainObject domainObject) throws Exception {
        RoomBooking roomBooking = (RoomBooking) domainObject;
        PreparedStatement updateStatement = connection.prepareStatement(
                "UPDATE room_booking SET is_active = ?, no_of_guests = ? , version = ? " +
                        "WHERE id = ? AND version = ? ");
        //"UPDATE room_booking SET is_active = " + roomBooking.getActive()  +  ", " +
        //                        "no_of_guests = " + roomBooking.getNumOfGuests() + " WHERE id = " + roomBooking.getId() + ";");

        int version = roomBooking.getVersion();
        int new_version = version + 1;

        updateStatement.setBoolean(1,roomBooking.getActive());
        updateStatement.setInt(2,roomBooking.getNumOfGuests());
        updateStatement.setInt(3,new_version);
        updateStatement.setInt(4,roomBooking.getId());
        updateStatement.setInt(5,version);
        System.out.println(updateStatement.toString());

        int row_count = updateStatement.executeUpdate();

        if(row_count==0)
            System.out.println("Concurrency issue");
        else
            System.out.println("Room Booking updated successfully");
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "DELETE FROM room_booking WHERE id = ? returning * ");
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                return true;
            }
            else
                try {
                    throw new Exception("Delete failed");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<RoomBooking> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception {
        RoomBookingSearchCriteria roomBookingSearchCriteria = (RoomBookingSearchCriteria) criteria;
        String findByCriteriaStatement = "SELECT r.id as id,h.name as hotel_name, start_date, end_date, " +
                "m.id as room_id, r.is_active, no_of_guests,main_guest, r.version AS version " +
                "FROM booking b " +
                "         JOIN ( room_booking r " +
                "    JOIN room m\n" +
                "    ON r.room_id=m.id) " +
                "              ON b.id = r.booking_id " +
                "         JOIN hotel h ON h.id = b.hotel_id " +
                "         JOIN app_user u ON u.id=b.customer_id ";

        PreparedStatement statement = null;
        if(roomBookingSearchCriteria.getBookingId()!=null) {
            statement = connection.prepareStatement(findByCriteriaStatement + " WHERE r.booking_id = ? ");
            statement.setInt(1,roomBookingSearchCriteria.getBookingId());
        }
        else if(roomBookingSearchCriteria.getRoomID()!=null && roomBookingSearchCriteria.getDate_range()!=null) {
            statement = connection.prepareStatement(findByCriteriaStatement +
                    " WHERE r.is_active = TRUE AND r.room_id = ? AND (b.start_date,b.end_date) " +
                    "OVERLAPS (?,?)");
            statement.setInt(1,roomBookingSearchCriteria.getRoomID());
            statement.setDate(2,roomBookingSearchCriteria.getDate_range().getFrom());
            statement.setDate(3,roomBookingSearchCriteria.getDate_range().getTo());
        }
        System.out.println("RB findby \n"+statement.toString());

        System.out.println("Room Booking Search Query");
        System.out.println(findByCriteriaStatement);

        ResultSet resultSet = statement.executeQuery();
        RoomBooking loadedRoomBooking = load(resultSet);
        while (loadedRoomBooking != null) {
            loadedRoomBooking = load(resultSet);
        }
        return new ArrayList<>(loadedMap.values());
    }

    @Override
    protected String findStatement() {
        return null;
    }

    @Override
    protected RoomBooking doLoad(Integer id, ResultSet resultSet) throws Exception {
        RoomBooking booking = new RoomBooking(
                id,
                dataSource,
                resultSet.getInt("room_id"),
                resultSet.getBoolean("is_active"),
                resultSet.getString("main_guest"),
                resultSet.getInt("no_of_guests"),
                resultSet.getInt("version")
        );
        return booking;
    }

    @Override
    public ArrayList<RoomBooking> findAll() throws SQLException {
        return null;
    }
}
