package lans.hotels.datasource.mappers;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.facade.IDataMapper;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.RoomSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;

import java.sql.*;
import java.util.ArrayList;

public class RoomDataMapper extends AbstractPostgresDataMapper<Room> implements IDataMapper<Room> {
    private static final String COLUMNS = " hotel_id, number, floor, is_active";

    public RoomDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "room", dataSource);
        idPrefix = "r.";
    }

    @Override
    protected String findStatement() {
        return "SELECT r.id AS id, number AS room_number , name AS hotel_name, \n" +
                "hotel_id, type, bed_type, max_occupancy, room_price, r.is_active from room r \n" +
                "JOIN hotel h ON h.id = r.hotel_id ";
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
        String findAllStatement = "SELECT " + " * " + " FROM " + this.table +" ";
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
        RoomSearchCriteria r_criteria = (RoomSearchCriteria) criteria;
        String findBy = findStatement() + " WHERE ";

        PreparedStatement statement = null;
        if (r_criteria.getHotelId() != null && r_criteria.getRoomNumber()==null){
            statement = connection.prepareStatement(findBy + "hotel_id = ?");
            statement.setInt(1,r_criteria.getHotelId());
        }
        else if (r_criteria.getRoomID() != null){
            statement = connection.prepareStatement(findBy + "r.id = ?");
            statement.setInt(1,r_criteria.getRoomID());
        }
        else if (r_criteria.getRoomNumber() != null){
            statement = connection.prepareStatement(findBy + "number = ? AND hotel_id = ? ");
            statement.setInt(1,r_criteria.getRoomNumber());
            statement.setInt(2,r_criteria.getHotelId());
        }

        System.out.println("FIND BY Statement \n"+statement.toString());
        ResultSet resultSet = statement.executeQuery();
        Room currentRoom = load(resultSet);
        while (currentRoom != null) {
            currentRoom = load(resultSet);
        }
        return new ArrayList<>(loadedMap.values());
    }

    @Override
    protected Room doLoad(Integer id, ResultSet rs) throws Exception {
        Room room = null;
        try {
            room = new Room(id,
                    dataSource,
                    rs.getInt("hotel_id"),
                    rs.getString("type"),
                    rs.getInt("max_occupancy"),
                    rs.getString("bed_type"),
                    rs.getInt("room_price"),
                    rs.getInt("room_number"),
                    rs.getBoolean("is_active"));
        } catch (UoWException e) {
            e.printStackTrace();
        }
        return room;
    }

    @Override
    public Room update(AbstractDomainObject domainObject) {
        return null;
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Integer insert(DomainObject domainObject) throws Exception {
        Room r = (Room) domainObject;
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO " +
                "    room(hotel_id, type, max_occupancy, bed_type, room_price , number, is_active) " +
                "SELECT ?, ?, ?, ?, ?, ?, ? " +
                " WHERE EXISTS ( " +
                " SELECT * from hotel WHERE id= ? AND is_active = TRUE " +
                "      ) " +
                "returning * ");

        statement.setInt(1,r.getHotelID());
        statement.setString(2,r.getType());
        statement.setInt(3,r.getMaxOccupancy());
        statement.setString(4,r.getBedType());
        statement.setInt(5,r.getRoomPrice());
        statement.setInt(6,r.getRoomNumber());
        statement.setBoolean(7,r.getIsActive());
        statement.setInt(8,r.getHotelID());

        System.out.println(statement.toString());
        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next())
        {
            return resultSet.getInt("id");
        }
        else
            throw new Exception("Insert failed");
//        if(resultSet.getFetchSize() == 0)
//        {
//            throw new Exception("Insert failed");
//        }
        //return resultSet.next() ? resultSet.getInt("id") : -1;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
