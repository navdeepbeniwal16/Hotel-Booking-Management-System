package lans.hotels.datasource.mappers;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.hotel.HotelBuilder;
import lans.hotels.domain.utils.Phone;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public Hotel update(Hotel domainObject) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
