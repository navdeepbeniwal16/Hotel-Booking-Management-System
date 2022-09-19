package lans.hotels.datasource.mappers;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.room.RoomBuilder;
import lans.hotels.domain.room.RoomSpecification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDataMapper extends AbstractPostgresDataMapper<Room> {
    private static final String COLUMNS = " hotel_id, number, floor, is_active, room_spec_id ";

    public RoomDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "room", dataSource);
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
    public Room doCreate(Room room) {
        // TODO: optimisation - can we not do this second call to the DB?
        Integer newRoomId = prepareAndExecuteInsertion(room);
        if (newRoomId != null) return getById(newRoomId);
        return null;
    }

    @Override
    public List<Room> findAll() throws SQLException {
        String findAllStatment = "SELECT " + " * " +
                " FROM " + this.table;
        try (PreparedStatement statement = connection.prepareStatement(findAllStatment)) {
            ResultSet resultSet = statement.executeQuery();
            while (load(resultSet) != null) {
                load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        }
    }

    @Override
    public List<Room> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception {
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
    protected Room doLoad(Integer id, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) return null;
        Hotel hotel = (Hotel) dataSource.find(Hotel.class, resultSet.getInt("hotel_id"));
        RoomSpecification specification = (RoomSpecification) dataSource.find(RoomSpecification.class, resultSet.getInt("room_spec_id"));
        RoomBuilder roomBuilder = new RoomBuilder(hotel, specification, dataSource);
        return roomBuilder
                .id(id)
                .number(resultSet.getInt("number"))
                .floor(resultSet.getInt("floor"))
                .active(resultSet.getBoolean("is_active"))
                .getResult();
    }

    @Override
    public Room update(Room domainObject) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
