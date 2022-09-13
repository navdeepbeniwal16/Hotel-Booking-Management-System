package lans.hotels.datasource.mappers;

import lans.hotels.domain.hotel.Hotel;

import java.sql.Connection;

public class HotelDataMapper extends AbstractPostgresDataMapper<Hotel> {
    private static final String COLUMNS = " number, floor, is_active, room_spec_id ";

    public HotelDataMapper(Connection connection) {
        super(connection, "hotel");
    }
    @Override
    protected String findStatement() {
        return "SELECT " + " id, " + COLUMNS +
                " FROM " + this.table +
                " WHERE id = ? ";
    }
}
