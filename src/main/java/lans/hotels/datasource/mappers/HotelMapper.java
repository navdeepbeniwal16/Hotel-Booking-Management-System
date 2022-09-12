package lans.hotels.datasource.mappers;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;

import java.sql.Connection;

public class HotelMapper extends AbstractPostgresMapper<Hotel> {
    private static final String COLUMNS = " number, floor, is_active, room_spec_id ";

    public HotelMapper(Connection connection, IDataSource dataSource) {
        super(connection, "hotel", dataSource);
    }
    @Override
    protected String findStatement() {
        return "SELECT " + " id, " + COLUMNS +
                " FROM " + this.table +
                " WHERE id = ? ";
    }
}
