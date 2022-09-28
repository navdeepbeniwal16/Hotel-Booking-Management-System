package lans.hotels.use_cases;
import lans.hotels.datasource.search_criteria.RoomSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.room.Room;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewHotelRoomsHotelier extends UseCase {

    Integer hotel_id;
    ArrayList<Room> rooms;

    public ViewHotelRoomsHotelier(IDataSource dataSource, Integer hotel_id) {
        super(dataSource);
        this.hotel_id = hotel_id;
        this.rooms = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {

        try {
            RoomSearchCriteria r_criteria = new RoomSearchCriteria();
            r_criteria.setHotelId(hotel_id);

            rooms = dataSource.findBySearchCriteria(Room.class,r_criteria);
            succeed();
        } catch (Exception e) {
            fail();
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    protected void constructResult() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (succeeded) {
                jsonArray = createHotelGroupsJSON();
            }
        } catch (Exception e) {
            fail();
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        } finally {
            succeeded();
            responseData.put("success",true);
            responseData.put("rooms", jsonArray);
        }
    }

    private JSONArray createHotelGroupsJSON() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (rooms == null) {
                System.err.println("null bookings");
            }
            rooms.forEach(room -> {
                JSONObject b_entry;
                b_entry = new JSONObject();
                b_entry.put("id", room.getId());
                b_entry.put("hotel_id", room.getHotelID());
                b_entry.put("room_number", room.getRoomNumber());
                b_entry.put("type", room.getType());
                b_entry.put("bed_type", room.getBedType());
                b_entry.put("max_occupancy", room.getMaxOccupancy());
                b_entry.put("room_price", room.getRoomPrice());
                b_entry.put("is_active", room.getIsActive());
                jsonArray.put(b_entry);
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            setResponseErrorMessage("error marshalling hotel data");
        }
        return jsonArray;
    }

}
