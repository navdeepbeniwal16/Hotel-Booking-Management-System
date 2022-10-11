package lans.hotels.use_cases;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.utils.Address;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetAllHotels extends UseCase {
    ArrayList<Hotel> hotels;

    public GetAllHotels(IDataSource dataSource) {
        super(dataSource);
        this.hotels = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {
        try {
            hotels = dataSource.findAll(Hotel.class);
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
            jsonArray = hotelsDTO();
            succeeded();
        } catch (Exception e) {
            fail();
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        }
        responseData.put("hotels", jsonArray);
    }

    private JSONArray hotelsDTO() {
        JSONArray hotels_array = new JSONArray();
        try {
            if (hotels != null) {
                hotels.forEach(hotel -> {
                    JSONObject hotel_entry = new JSONObject();
                    hotel_entry.put("id", hotel.getID());
                    hotel_entry.put("hotel_group_id", hotel.getHotelGroupID());
                    hotel_entry.put("name", hotel.getName());
                    hotel_entry.put("email", hotel.getEmail());
                    hotel_entry.put("phone", hotel.getContact());
                    hotel_entry.put("pincode", hotel.getPinCode());
                    hotel_entry.put("is_active", hotel.getIsActive());
                    hotel_entry.put("version",hotel.getVersion());

                    JSONObject addressJson = new JSONObject();
                    Address address = hotel.getAddress();
                    addressJson.put("line_1", address.getLine1() != null ? address.getLine1() : "");
                    addressJson.put("line_2", address.getLine2() != null ? address.getLine2() : "");
                    addressJson.put("city", address.getCity() != null ? address.getCity() : "");
                    addressJson.put("district", address.getDistrict().toString());
                    addressJson.put("postcode", address.getPostCode() != null ? Integer.toString(address.getPostCode()) : "");

                    hotel_entry.put("address", addressJson);
                    hotels_array.put(hotel_entry);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            setResponseErrorMessage("error marshalling hotel data");
        }
        return hotels_array;
    }
}
