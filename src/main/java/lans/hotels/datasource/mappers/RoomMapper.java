package lans.hotels.datasource.mappers;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;

import java.sql.*;

public class RoomMapper extends AbstractPostgresMapper<Room> {
    private static final String COLUMNS = " hotel_id, number, floor, is_active, room_spec_id ";

    public RoomMapper(Connection connection) {
        super(connection, "room");
    }

    @Override
    protected String findStatement() {
        return "SELECT " + " id, " + COLUMNS +
                " FROM " + this.table +
                " WHERE id = ? ";
    }

    @Override
    protected String insertStatement() {
        String columnValues = " ?, ?, ?, ?, ? ";
        return "INSERT INTO " + this.table + "(" + COLUMNS + ")" +
                " VALUES "  + " ( " + columnValues + " )" +
                " RETURNING id; ";
    }

    public Room concreteCreate(Room room) {
        // TODO: optimisation - can we not do this second call to the DB?
        Integer newRoomId = prepareAndExecuteInsertion(room);
        if (newRoomId != null) return getById(newRoomId);
        System.out.println("--- bad create");
        return null;
    }

    private Integer prepareAndExecuteInsertion(Room room) {
        try (PreparedStatement insertStatement = connection.prepareStatement(insertStatement())){
            insertStatement.setInt(1, room.getHotel().getUid());
            insertStatement.setInt(2, room.getRoomNumber());
            insertStatement.setInt(3, room.getRoomFloor());
            insertStatement.setBoolean(4, true);
            insertStatement.setInt(5, room.getSpecificationId());
            ResultSet keys = insertStatement.executeQuery();
            if (keys.next()) return keys.getInt("id");
            System.err.println("RoomMapper error: insertion did not return id");
            return null;
        } catch (SQLException e) {
            System.err.println("Room.prepareAndExecuteInsertion(room): " + e.getMessage());
            return null;
        }
    }

    @Override
    protected Room doLoad(int id, ResultSet resultSet) throws SQLException {
        int hotelId = resultSet.getInt("hotel_id");
        int specificationId = resultSet.getInt("room_spec_id");
        int roomNumber = resultSet.getInt("number");
        int roomFloor = resultSet.getInt("floor");
        boolean isActive = resultSet.getBoolean("is_active");
        return new Room(new Hotel(hotelId), id, specificationId, roomNumber, roomFloor, isActive);
    }
}
