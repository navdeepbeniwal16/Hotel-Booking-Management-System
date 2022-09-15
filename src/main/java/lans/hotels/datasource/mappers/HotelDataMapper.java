package lans.hotels.datasource.mappers;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;

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
        return "SELECT " + " id, " + COLUMNS +
                " FROM " + this.table +
                " WHERE id = ? ";
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    public Hotel concreteCreate(Hotel domainObject) {
        return null;
    }

    @Override
    protected Hotel doLoad(int id, ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    protected Hotel doLoad(Integer id, ResultSet resultSet) throws SQLException {
        return null;
    }
}
