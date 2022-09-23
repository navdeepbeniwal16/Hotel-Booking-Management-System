package lans.hotels.datasource.mappers;
import lans.hotels.datasource.exceptions.UoWException;
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
        return "SELECT " + " * " +
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
    public ArrayList<Room> findAll() throws SQLException {
        String findAllStatement = "SELECT " + " * " + " FROM " + this.table +
                " JOIN room_spec rs ON room_spec_id=rs.id ";
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
        } catch (Exception e) {
            throw new RuntimeException(e);
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
    protected Room doLoad(Integer id, ResultSet resultSet) throws Exception {
        System.out.println("RoomDataMapper.doLoad():");
        for (int i = 1; i < resultSet.getMetaData().getColumnCount(); i++) {
            System.out.println("\t" + i + ". " + resultSet.getMetaData().getColumnName(i));
        }
        if (!resultSet.next()) return null;
        ArrayList<Hotel> hotels = dataSource.findAll(Hotel.class);

        Hotel hotel = null;
        for(Hotel h: hotels) {
            if (h.getId()==resultSet.getInt("hotel_id")) {
                hotel = h;
            }
        }
        if (hotel == null) {
            throw new Exception("ERROR - no hotel with id = " + resultSet.getInt("hotel_id"));
        }

        RoomSpecification specification = new RoomSpecification(
                resultSet.getInt("room_spec_id"),
                resultSet.getInt("hotel_id"),
                resultSet.getInt("max_occupancy"),
                resultSet.getString("bed_type"),
                resultSet.getString("type"),
                resultSet.getInt("room_price"),
                dataSource
        );

        return new Room(
                hotel,
                specification,
                resultSet.getInt("number"),
                resultSet.getInt("floor"),
                resultSet.getBoolean("is_active"),
                id,
                dataSource
        );
    }

    @Override
    public Room update(AbstractDomainObject domainObject) {
        return null;
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Boolean insert(DomainObject domainObject) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
