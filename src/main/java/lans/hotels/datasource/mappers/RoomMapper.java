package lans.hotels.datasource.mappers;
import lans.hotels.datasource.identity_maps.HotelMap;
import lans.hotels.datasource.identity_maps.RoomSpecificationMap;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.room.RoomBuilder;
import lans.hotels.domain.room.RoomSpecification;

import java.sql.*;

public class RoomMapper extends AbstractPostgresMapper<Room> {
    private HotelMap hotels;
    private RoomSpecificationMap roomSpecifications;
    private static final String COLUMNS = " hotel_id, number, floor, is_active, room_spec_id ";

    public RoomMapper(Connection connection, HotelMap hotelMap, RoomSpecificationMap roomSpecificationMap) {
        super(connection, "room");
        this.hotels = hotelMap;
        this.roomSpecifications = roomSpecificationMap;
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
            insertStatement.setInt(5, room.getSpecification().getUid());
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
        Room room = roomBuilder
                .number(resultSet.getInt("number"))
                .floor(resultSet.getInt("floor"))
                .active(resultSet.getBoolean("is_active"))
                .getResult();
        return room;
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
