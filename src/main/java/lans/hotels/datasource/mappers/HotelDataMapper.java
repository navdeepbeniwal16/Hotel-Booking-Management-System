package lans.hotels.datasource.mappers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.District;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HotelDataMapper extends AbstractPostgresDataMapper<Hotel> {

    public HotelDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "hotel", dataSource);
        idPrefix = "h.";
    }
    @Override
    protected String findStatement() {
        String statement =
                "SELECT h.id,hotel_group_id,h.name as hotel_name, " +
                        "email,contact,h.city as hotel_city,pin_code,is_active, " +
                        "a.id as address_id,line_1,line_2,a.city as address_city, " +
                        "postcode,d.id as district_id, d.name as district_name " +
                        "FROM hotel h " +
                        "JOIN (address a " +
                        "JOIN district d ON a.district = d.id) " +
                        "on h.address = a.id ";
        System.out.println("HotelDataMapper.findStatement(): " + statement);
        return statement;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    public ArrayList<Hotel> findAll() throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(findStatement())) {
            ResultSet resultSet = statement.executeQuery();
            Hotel aHotel = load(resultSet);
            while (aHotel != null) {
                System.out.println(aHotel);
                aHotel = load(resultSet);
            }
        } catch (Exception e) {
            System.err.println("ERROR HotelDataMapper.findAll(): " + e);
        } finally {
            System.out.println("HotelDataMapper.findAll() - loaded hotels: " + loadedMap);
        }
        return new ArrayList<>(loadedMap.values());
    }

    @Override
    public ArrayList<Hotel> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception {
        HotelSearchCriteria hotelSearchCriteria = (HotelSearchCriteria) criteria;
        String findBy = findStatement() + "WHERE ";

        PreparedStatement statement = null;
        if (hotelSearchCriteria.getId() != null){
            statement = connection.prepareStatement(findBy +"h.id = ?");
            statement.setInt(1,hotelSearchCriteria.getId());
        }
        else if (hotelSearchCriteria.getCity() != null){
            statement = connection.prepareStatement(findBy + "h.city = ?");
            statement.setString(1,hotelSearchCriteria.getCity());
        }
        else if (hotelSearchCriteria.getHotelGroupId() != null){
            statement = connection.prepareStatement(findBy + "h.hotel_group_id = ?");
            statement.setInt(1,hotelSearchCriteria.getHotelGroupId());
        }

        System.out.println("Query : "+statement.toString());

        ResultSet resultSet = statement.executeQuery();
        Hotel currentHotel = load(resultSet);
        while (currentHotel != null) {
            currentHotel = load(resultSet);
        }
        return new ArrayList<>(loadedMap.values());

    }

    @Override
    protected Hotel doLoad(Integer id, ResultSet rs) throws SQLException {
        District district = new District(rs.getString("district_name"));
        Address address = new Address(rs.getString("line_1"),
                rs.getString("line_2"),
                district,
                rs.getString("address_city"),
                rs.getInt("postcode"));

        Hotel hotel = null;
        try {
            hotel = new Hotel(id,dataSource,
                    rs.getInt("hotel_group_id"),rs.getString("hotel_name"),
                    rs.getString("email"),address,rs.getString("contact"),
                    rs.getString("hotel_city"),rs.getInt("pin_code"),
                    rs.getBoolean("is_active"));
        } catch (UoWException e) {
            e.printStackTrace();
        }
        return hotel;
    }

    @Override
    public Hotel update(AbstractDomainObject domainObject) {
        Hotel hotel = (Hotel) domainObject;
        String updateStatement = "UPDATE hotel SET is_active = " + hotel.getIsActive()  +  " WHERE id = " + hotel.getId() + ";";

        System.out.println("UPDATE BOOKING QUERY : ");
        System.out.println(updateStatement);

        try (PreparedStatement statement = connection.prepareStatement(updateStatement)) {
            statement.executeUpdate();
            System.out.println("Hotel mapper : Hotel with id " + hotel.getId() + " updated in DataMapper...");

            HotelSearchCriteria criteria = new HotelSearchCriteria();
            criteria.setId(hotel.getId());

            ArrayList<Hotel> hotels = findBySearchCriteria(criteria);
            if(hotels.size() > 0) return hotels.get(0);
            else return null;
        } catch (Exception e) {
            System.out.println("Exception occurred at BookingDataMapper:update() execution");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Boolean insert(DomainObject domainObject) throws SQLException {
        Hotel h = (Hotel) domainObject;
        PreparedStatement statement = connection.prepareStatement("WITH insert_address AS ( " +
                "INSERT INTO address (line_1, line_2, district, city, postcode) " +
                "VALUES ( ?, ? , ( SELECT id from district WHERE name = ? ), ? , ? ) RETURNING id ) " +
                "INSERT INTO hotel (hotel_group_id, name, email, address, contact, city, pin_code, is_active) " +
                "VALUES ( ? , ? , ? , (SELECT id FROM insert_address) , ? , ? , ? , ? ) returning * ");


        statement.setString(1,h.getAddress().getLine1());
        statement.setString(2,h.getAddress().getLine2());
        statement.setString(3,h.getAddress().getDistrict().toString());
        statement.setString(4,h.getAddress().getCity());
        statement.setInt(5,h.getAddress().getPostCode());
        statement.setInt(6,h.getHotelGroupID());
        statement.setString(7,h.getName());
        statement.setString(8,h.getEmail());
        statement.setString(9,h.getContact());
        statement.setString(10,h.getCity());
        statement.setInt(11,h.getPinCode());
        statement.setBoolean(12,h.getIsActive());

        System.out.println(statement.toString());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next())
                return true;
            else return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
