package lans.hotels.datasource.mappers;
import java.sql.Connection;
public class RoomMapper extends AbstractMapper {
    private static final String COLUMNS = " room_number, room_floor, isActive, room_spec_id ";

    public RoomMapper() {
        // Connection connection
//        super(connection);
    }
    protected String findStatement() {
        return "SELECT " + COLUMNS +
                " FROM room " +
                " WHERE room_id = ? ";

    }
}
