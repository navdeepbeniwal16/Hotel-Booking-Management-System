package lans.hotels.datasource.mappers;
import lans.hotels.domain.hotel.Room;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomMapper extends AbstractPostgresMapper<Room> {
    private static final String COLUMNS = " id, number, floor, is_active, room_spec_id ";

    public RoomMapper(Connection connection) {
        super(connection);
    }

    @Override
    protected String findStatement() {
        return "SELECT " + COLUMNS +
                " FROM room " +
                " WHERE id = ? ";
    }

    @Override
    public Room getById(int id) {
        try {
            return abstractGetById(id);
        } catch (SQLException e) {
            System.out.println("RoomMapper.getById(): " + e.getMessage());
            // TODO: do not return null!
            return null;
        }
    }

    @Override
    protected Room doLoad(int id, ResultSet resultSet) throws SQLException {
        int specificationId = resultSet.getInt("room_spec_id");
        int roomNumber = resultSet.getInt("number");
        int roomFloor = resultSet.getInt("floor");
        boolean isActive = resultSet.getBoolean("is_active");
        return new Room(id, specificationId, roomNumber, roomFloor, isActive);
    }
}
