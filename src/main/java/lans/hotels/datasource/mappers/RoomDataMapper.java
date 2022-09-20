package lans.hotels.datasource.mappers;
import lans.hotels.datasource.facade.IDataMapper;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.room.RoomBuilder;
import lans.hotels.domain.room.RoomSpecification;

import java.sql.*;
import java.util.ArrayList;

public class RoomDataMapper extends AbstractPostgresDataMapper<Room> implements IDataMapper<Room> {
    private static final String COLUMNS = " hotel_id, number, floor, is_active, room_spec_id ";

    public RoomDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "room", dataSource);
        idPrefix = "r.";
    }

    @Override
    protected String findStatement() {
        return "SELECT " + " * " + COLUMNS +
                " FROM " + this.table + " r " +
                " JOIN room_spec rs ON r.room_spec_id = rs.id" +
                " WHERE r.id = ? ";
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
    public ArrayList<Room> findAll() throws SQLException {
        String findAllStatement = "SELECT " + " * " + " FROM " + this.table +
                " JOIN room_spec rs ON room_spec_id=rs.id " +
                " JOIN room_type rt ON rs.room_type=rt.id" ;
        System.out.println("RoomDataMapper.findAll(): " + findAllStatement);
        try (PreparedStatement statement = connection.prepareStatement(findAllStatement)) {
            ResultSet resultSet = statement.executeQuery();
            System.out.println("RoomDataMapper.findAll(): " + resultSet.toString());
            while (load(resultSet) != null) {
                load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        } catch (SQLException e) {
            System.err.println("RoomMapper.findAll():" + e);
            throw e;
        }
    }

    @Override
    public ArrayList<Room> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception {
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
        Hotel hotel = dataSource.find(Hotel.class, resultSet.getInt("hotel_id"));
        RoomSpecification specification = dataSource.find(RoomSpecification.class, resultSet.getInt("rs.id"));
        RoomBuilder roomBuilder = new RoomBuilder(hotel, specification, dataSource);
        return roomBuilder
                .id(id)
                .number(resultSet.getInt("r.number"))
                .floor(resultSet.getInt("r.floor"))
                .active(resultSet.getBoolean("r.is_active"))
                .getResult();
    }

    @Override
    public Room update(AbstractDomainObject domainObject) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
