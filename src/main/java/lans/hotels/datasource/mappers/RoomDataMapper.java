package lans.hotels.datasource.mappers;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.room.RoomBuilder;
import lans.hotels.domain.room.RoomSpecification;

import java.sql.*;

public class RoomDataMapper extends AbstractPostgresDataMapper<Room> {
    private static final String COLUMNS = " hotel_id, number, floor, is_active, room_spec_id ";

    public RoomDataMapper(Connection connection) {
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

    @Override
    public Room concreteCreate(Room room) {
        // TODO: optimisation - can we not do this second call to the DB?
        Integer newRoomId = prepareAndExecuteInsertion(room);
        if (newRoomId != null) return getById(newRoomId);
        return null;
    }

    private Integer prepareAndExecuteInsertion(Room room) {
        try (PreparedStatement insertStatement = connection.prepareStatement(insertStatement())){
            insertStatement.setInt(1, room.getHotel().getId());
            insertStatement.setInt(2, room.getRoomNumber());
            insertStatement.setInt(3, room.getRoomFloor());
            insertStatement.setBoolean(4, true);
            insertStatement.setInt(5, room.getSpecification().getId());
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
        Hotel hotel = lookupHotel(resultSet.getInt("hotel_id"));
        RoomSpecification specification = lookupRoomSpecification(resultSet.getInt("room_spec_id"));
        RoomBuilder roomBuilder = new RoomBuilder(hotel, specification);
        return roomBuilder
                .number(resultSet.getInt("number"))
                .floor(resultSet.getInt("floor"))
                .active(resultSet.getBoolean("is_active"))
                .getResult();
    }

    private Hotel lookupHotel(Integer hotelId) {
        Hotel hotel = this.hotels.get(hotelId);
        if (hotel == null) {
            hotel = new Hotel(hotelId);
            hotels.add(hotel);
        }
        return hotel;
    }

    private RoomSpecification lookupRoomSpecification(Integer roomSpecId) {
        RoomSpecification roomSpec = roomSpecifications.get(roomSpecId);
        if (roomSpec == null) {
            roomSpec = new RoomSpecification(roomSpecId);
            roomSpecifications.add(roomSpec);
        }
        return roomSpec;
    }
}
