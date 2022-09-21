package lans.hotels.datasource.mappers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelsSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.hotel_group.HotelGroup;
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
    public ArrayList<Hotel> insert() throws Exception {
        return null;
    }

    @Override
    public ArrayList<Hotel> findBySearchCriteria(AbstractSearchCriteria criteria){
        HotelsSearchCriteria hotelsSearchCriteria = (HotelsSearchCriteria) criteria;
        String findAllStatement = findStatement();

        if (hotelsSearchCriteria.getHotelId() != null){
            findAllStatement += "WHERE h.id = '" + hotelsSearchCriteria.getHotelId() + "'";
            System.out.println(findAllStatement);
            System.out.println("ID passed to HotelDataMapper : " + hotelsSearchCriteria.getHotelId());
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
                rs.getString("hotel_city"),rs.getInt("pin_code"),
                rs.getBoolean("is_active"));
        return hotel;
    }

    @Override
    public Hotel update(AbstractDomainObject domainObject) {
        Hotel hotel = (Hotel) domainObject;
        String updateStatement = "UPDATE hotel SET is_active = " + hotel.getIsActive()  +  " WHERE id = " + hotel.getId() + ";";

        System.out.println("UPDATE BOOKING QUERY : ");
        System.out.println(updateStatement);

        try (PreparedStatement statement = connection.prepareStatement(updateStatement)) {
            statement.executeQuery();
            System.out.println("Hotel mapper : Hotel with id " + hotel.getId() + " updated in DataMapper...");

            HotelsSearchCriteria criteria = new HotelsSearchCriteria();
            criteria.setHotelId(hotel.getId());

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
    public <DomainObject extends AbstractDomainObject> Boolean create(DomainObject domainObject) throws SQLException, UoWException {
        Hotel h = (Hotel) domainObject;
        String createStatement = "WITH insert_address AS ( " +
                "INSERT INTO address (line_1,line_2,district,city,postcode) " +
                "VALUES ( " + "'" + h.getAddress().getLine1() +"'," +
                "'" + h.getAddress().getLine2() + "'," + "( SELECT id from district WHERE name = '" +
                h.getAddress().getDistrict().toString()+"')," + "'" + h.getAddress().getCity() + "" +"'," +
                h.getAddress().getPostCode() +")" +
                " RETURNING id )" +
                " INSERT INTO hotel (hotel_group_id, name, email, address,contact,city,pin_code,is_active) VALUES ('" +
                h.getHotelGroupID() + "'," + "'" +h.getName() + "'," + "'" + h.getEmail() +"',"
                +"(SELECT id FROM insert_address)" + "," + "'" + h.getContact() + "'," +
                "'" + h.getCity() + "'," + h.getPinCode() +"," + h.getIsActive() +") returning * ";

        System.out.println(createStatement);
        try (PreparedStatement statement = connection.prepareStatement(createStatement)) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return true;
            else return false;

        }
        catch (SQLException e)
        {
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
