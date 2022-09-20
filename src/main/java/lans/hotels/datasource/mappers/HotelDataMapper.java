package lans.hotels.datasource.mappers;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelsSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.hotel.HotelBuilder;
import lans.hotels.domain.utils.Phone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HotelDataMapper extends AbstractPostgresDataMapper<Hotel> {
    private static final String COLUMNS = " number, floor, is_active, room_spec_id ";

    public HotelDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "hotel", dataSource);
    }
    @Override
    protected String findStatement() {
        return "SELECT " + " * " +
                " FROM " + this.table + " h " +
                " JOIN phone p ON h.phone = p.id " +
                " JOIN address a on h.address = a.id " +
                " WHERE h.id = ? ";
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
        String findAllStatment = "SELECT " + " * " +
                " FROM " + this.table + " h " +
                " JOIN phone p ON h.phone = p.id " +
                " JOIN address a on h.address = a.id ";
        try (PreparedStatement statement = connection.prepareStatement(findAllStatment)) {
            ResultSet resultSet = statement.executeQuery();
            Hotel aHotel = load(resultSet);
            while (aHotel != null) {
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
        HotelsSearchCriteria hotelsSearchCriteria = (HotelsSearchCriteria) criteria;
        String findAllStatment = "SELECT " + " * " +
                " FROM " + this.table + " h " +
                " JOIN phone p ON h.phone = p.id " +
                " JOIN address a on h.address = a.id ";

        if (hotelsSearchCriteria.getLocation() != null){
            findAllStatment += "WHERE city = '" + hotelsSearchCriteria.getLocation() + "'";
            System.out.println("Location passed to HotelDataMapper : " + hotelsSearchCriteria.getLocation());
        }

        if(hotelsSearchCriteria.getHotelGroupId() != null) {
            findAllStatment += "WHERE hotel_group_id = '" + hotelsSearchCriteria.getHotelGroupId() + "'";
            System.out.println("HotelGroupId passed to HotelDataMapper : " + hotelsSearchCriteria.getLocation());
        }

        if(hotelsSearchCriteria.getStartDate() != null && hotelsSearchCriteria.getEndDate() != null) {
            // TODO: Add a query that will only get the hotels that have rooms not present in the bookings for a given date range
            // Check with Saood about how we can do this
        }


        try (PreparedStatement statement = connection.prepareStatement(findAllStatment)) {
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
        HotelBuilder builder = new HotelBuilder(dataSource);
        Phone phone = new Phone(rs.getInt("country"),
                rs.getInt("area"),
                rs.getInt("number"));
        Hotel hotel = builder.id(id)
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .phone(phone)
                .address(rs.getString("line_1"))
                .getResult();
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
