package lans.hotels.use_cases;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ViewHotelRooms extends UseCase {
    Integer hotel_id;
    ArrayList<Room> rooms;
    Utils.RoomResultsInclude include;
    Date startDate;
    Date endDate;

    public ViewHotelRooms(IDataSource dataSource, Integer hotel_id) {
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
                } else {
                    assert endDate != null && startDate != null;
                    rooms = include == Utils.RoomResultsInclude.AVAILABLE ?
                            hotel.getAllAvailableRooms(startDate, endDate) :
                            hotel.getAllUnAvailableRooms(startDate, endDate);
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
        try {
            if (succeeded) responseData.put("rooms", createHotelGroupsJSON());
        } catch (Exception e) {
            fail();
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        }
    }

    private JSONArray createHotelGroupsJSON() {
        JSONArray jsonArray = new JSONArray();
        try {
            rooms.forEach(room -> {
                JSONObject roomObject;
                roomObject = new JSONObject();
                roomObject.put("id", room.getId());
                roomObject.put("hotel_id", room.getHotelID());
                roomObject.put("number", room.getRoomNumber());
                roomObject.put("type", room.getType());
                roomObject.put("bed_type", room.getBedType());
                roomObject.put("max_occupancy", room.getMaxOccupancy());
                roomObject.put("price", room.getRoomPrice());
                roomObject.put("is_active", room.getIsActive());
                jsonArray.put(roomObject);
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            setResponseErrorMessage("error marshalling hotel data");
        }
        return jsonArray;
    }

}
