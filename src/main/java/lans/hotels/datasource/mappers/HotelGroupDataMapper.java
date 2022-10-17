package lans.hotels.datasource.mappers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelGroupSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.District;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HotelGroupDataMapper extends AbstractPostgresDataMapper<HotelGroup> {

    public HotelGroupDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "hotel_group", dataSource);
        idPrefix = "h.";
    }
    @Override
    protected String findStatement() {
        String statement =
                "SELECT hg.id,hg.name as hotel_group_name, " +
                        "a.id as address_id,line_1,line_2,a.city as address_city, " +
                        "postcode,d.id as district_id, d.name as district_name, phone " +
                        "FROM hotel_group hg " +
                        "JOIN (address a " +
                        "JOIN district d ON a.district = d.id) " +
                        "on hg.address = a.id ";
        return statement;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    public ArrayList<HotelGroup> findAll() throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(findStatement())) {
            ResultSet resultSet = statement.executeQuery();
            HotelGroup aHotelGroup = load(resultSet);
            while (aHotelGroup != null) {
                aHotelGroup = load(resultSet);
            }
        } catch (Exception e) {
            System.err.println("ERROR HotelGroupDataMapper.findAll(): " + e);
            e.printStackTrace();
        }
        return new ArrayList<>(loadedMap.values());
    }

    @Override
    public ArrayList<HotelGroup> findBySearchCriteria(AbstractSearchCriteria criteria){
        HotelGroupSearchCriteria hgCriteria = (HotelGroupSearchCriteria) criteria;
        String findAllStatement = findStatement();

        if (hgCriteria.getHotelGroupID() != null){
            findAllStatement += "WHERE hg.id = '" + hgCriteria.getHotelGroupID() + "'";
        }
        else if (hgCriteria.getHotelGroupName() != null){
            findAllStatement += "WHERE hg.name = '" + hgCriteria.getHotelGroupName() + "'";
        }

        try (PreparedStatement statement = connection.prepareStatement(findAllStatement)) {
            ResultSet resultSet = statement.executeQuery();
            HotelGroup currentHotelGroup = load(resultSet);
            while (currentHotelGroup != null) {
                currentHotelGroup = load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected HotelGroup doLoad(Integer id, ResultSet rs) throws SQLException, UoWException {
        District district = new District(rs.getString("district_name"));
        Address address = new Address(rs.getString("line_1"),
                rs.getString("line_2"),
                district,
                rs.getString("address_city"),
                rs.getInt("postcode"));

        HotelGroup hotelGroup = new HotelGroup(id,dataSource,
                rs.getString("hotel_group_name"),address,rs.getString("phone"));
        return hotelGroup;
    }

    @Override
    public HotelGroup update(AbstractDomainObject domainObject) {
        return null;
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Integer insert(DomainObject domainObject) throws Exception {
        HotelGroup hg = (HotelGroup) domainObject;
        PreparedStatement statement = connection.prepareStatement(
                "WITH insert_address AS ( " +
                        "INSERT INTO address (line_1,line_2,district,city,postcode) " +
                        "VALUES ( ?, ?, ( SELECT id from district WHERE name = ? ), ?, ?)" +
                        " RETURNING id )" +
                        "INSERT INTO hotel_group (name, address,phone) VALUES ( ? ," +
                        " (SELECT id FROM insert_address), ?) returning * ");

        statement.setString(1,hg.getAddress().getLine1());
        statement.setString(2,hg.getAddress().getLine2());
        statement.setString(3,hg.getAddress().getDistrict().toString());
        statement.setString(4,hg.getAddress().getCity());
        statement.setInt(5,hg.getAddress().getPostCode());
        statement.setString(6,hg.getName());
        statement.setString(7,hg.getPhone());

        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next())
        {
            return resultSet.getInt("id");
        }
        else
            throw new Exception("Insert failed");
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
