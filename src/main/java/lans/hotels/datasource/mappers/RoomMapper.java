package lans.hotels.datasource;

public class RoomMapper extends {
    private static final String COLUMNS = " room_id, room_number, room_floor, isActive, room_spec_id ";
    protected String findStatement() {
        return "SELECT " + COLUMNS +
                " FROM room " +

    }
}
