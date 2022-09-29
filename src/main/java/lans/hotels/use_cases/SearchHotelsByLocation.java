package lans.hotels.use_cases;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchHotelsByLocation extends UseCase {

    String city;
    ArrayList<Hotel> hotels;

    public SearchHotelsByLocation(IDataSource dataSource,String city) {
        super(dataSource);
        this.city = city;
        this.hotels = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {

        HotelSearchCriteria hotel_criteria = new HotelSearchCriteria();
        hotel_criteria.setCity(city);
        try {
            hotels = dataSource.findBySearchCriteria(Hotel.class,hotel_criteria);
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
            responseData.put("hotels", jsonArray);
        }
    }

    private JSONArray createHotelGroupsJSON() {
        JSONArray hotels_array = new JSONArray();
        try {
            if (hotels == null) {
                System.err.println("null hotels");
            }
            hotels.forEach(hotel -> {
                JSONObject hotel_entry = new JSONObject();
                hotel_entry.put("id", hotel.getID());
                hotel_entry.put("hotel_group_id", hotel.getHotelGroupID());
                hotel_entry.put("name", hotel.getName());
                hotel_entry.put("email", hotel.getEmail());
                hotel_entry.put("phone", hotel.getContact());
                hotel_entry.put("pincode", hotel.getPinCode());
                hotel_entry.put("is_active", hotel.getIsActive());

                JSONObject addressJson = new JSONObject();
                addressJson.put("line_1", hotel.getAddress().getLine1());
                addressJson.put("line_2", hotel.getAddress().getLine2());
                addressJson.put("city", hotel.getAddress().getCity());
                addressJson.put("district", hotel.getAddress().getDistrict());
                addressJson.put("postcode", hotel.getAddress().getPostCode());

                hotel_entry.put("address", addressJson);
                hotels_array.put(hotel_entry);
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            setResponseErrorMessage("error marshalling hotel data");
        }
        return hotels_array;
    }

}
