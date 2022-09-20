package lans.hotels.datasource.mappers;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelsSearchCriteria;
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
                        "email,contact,h.city as hotel_city,pin_code, " +
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
    public Hotel doCreate(Hotel domainObject) {
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
    public ArrayList<Hotel> findBySearchCriteria(AbstractSearchCriteria criteria){
        HotelsSearchCriteria hotelsSearchCriteria = (HotelsSearchCriteria) criteria;
        String findAllStatement = findStatement();

        if (hotelsSearchCriteria.getHotelId() != null){
            findAllStatement += "WHERE h.id = '" + hotelsSearchCriteria.getHotelId() + "'";
            System.out.println("Location passed to HotelDataMapper : " + hotelsSearchCriteria.getLocation());
        }

        if (hotelsSearchCriteria.getLocation() != null){
            findAllStatement += "WHERE h.city = '" + hotelsSearchCriteria.getLocation() + "'";
            System.out.println("Location passed to HotelDataMapper : " + hotelsSearchCriteria.getLocation());
        }


        try (PreparedStatement statement = connection.prepareStatement(findAllStatement)) {
            ResultSet resultSet = statement.executeQuery();
            Hotel currentHotel = load(resultSet);
            while (currentHotel != null) {
                currentHotel = load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        } catch (Exception e) {
            System.out.println("Exception occurred at findBySearchCriteria");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected Hotel doLoad(Integer id, ResultSet rs) throws SQLException {
        District district = new District(rs.getString("district_name"));
        Address address = new Address(rs.getString("line_1"),
                rs.getString("line_2"),
                district,
                rs.getString("address_city"),
                rs.getInt("postcode"));

        Hotel hotel = new Hotel(id,dataSource,
                rs.getInt("hotel_group_id"),rs.getString("hotel_name"),
                rs.getString("email"),address,rs.getString("contact"),
                rs.getString("hotel_city"),rs.getInt("pin_code"));
        return hotel;
    }

    @Override
    public Hotel update(AbstractDomainObject domainObject) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
