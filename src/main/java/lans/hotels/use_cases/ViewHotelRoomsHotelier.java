package lans.hotels.use_cases;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.datasource.search_criteria.RoomSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ViewHotelRoomsHotelier extends UseCase {
    Integer hotel_id;
    ArrayList<Room> rooms;
    Utils.RoomResultsInclude include;
    Date startDate;
    Date endDate;

    public ViewHotelRoomsHotelier(IDataSource dataSource, Integer hotel_id) {
        super(dataSource);
        this.hotel_id = hotel_id;
        this.rooms = new ArrayList<>();
    }

    public void setInclude(Utils.RoomResultsInclude include) {
        this.include = include;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public void doExecute() throws Exception {
        try {
            HotelSearchCriteria hotelSearchCriteria = new HotelSearchCriteria();
            hotelSearchCriteria.setId(hotel_id);
            ArrayList<Hotel> hotels = dataSource.findBySearchCriteria(Hotel.class, hotelSearchCriteria);
            if(hotels.size() > 0) {
                Hotel hotel = hotels.get(0);

                if(include == Utils.RoomResultsInclude.ALL) {
                    rooms = hotel.getAllRooms();
                } else if(include == Utils.RoomResultsInclude.AVAILABLE) {
                    rooms = hotel.getAllAvailableRooms(startDate, endDate);
                } else {
                    rooms = hotel.getAllUnAvailableRooms(startDate, endDate);
                }
            }

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
