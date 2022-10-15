package lans.hotels.use_cases;

import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.datasource.search_criteria.UserSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.user.User;
import lans.hotels.domain.utils.Address;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewHotelGroupHotels extends UseCase {

    Integer hg_id;
    ArrayList<Hotel> hotels;


    public ViewHotelGroupHotels(IDataSource dataSource,Integer hg_id) {
        super(dataSource);
        this.hg_id = hg_id;
        this.hotels = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {
        HotelSearchCriteria h_criteria = new HotelSearchCriteria();
        h_criteria.setHotelGroupId(hg_id);
        try {
            hotels = dataSource.findBySearchCriteria(Hotel.class, h_criteria);
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
            if (succeeded) jsonArray = createHotelGroupsJSON();
        } catch (Exception e) {
            fail();
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        } finally {
            responseData.put("hotels", jsonArray);
        }
    }

    private JSONArray createHotelGroupsJSON() {
        JSONArray hotelsDTO = new JSONArray();
        try {
            if (succeeded && hotels != null) {
                hotels.forEach(hotel -> {
                    JSONObject hotelDTO = new JSONObject();
                    hotelDTO.put("id", hotel.getID());
                    hotelDTO.put("hotel_group_id", hotel.getHotelGroupID());
                    hotelDTO.put("name", hotel.getName());
                    hotelDTO.put("email", hotel.getEmail());
                    hotelDTO.put("phone", hotel.getContact());
                    hotelDTO.put("pincode", hotel.getPinCode());
                    hotelDTO.put("is_active", hotel.getIsActive());
                    hotelDTO.put("version",hotel.getVersion());

                    JSONObject addressJson = new JSONObject();
                    Address address = hotel.getAddress();
                    addressJson.put("line_1", address.getLine1() != null ? address.getLine1() : "");
                    addressJson.put("line_2", address.getLine2() != null ? address.getLine2() : "");
                    addressJson.put("city", address.getCity() != null ? address.getCity() : "");
                    addressJson.put("district", address.getDistrict().toString());
                    addressJson.put("postcode", address.getPostCode() != null ? Integer.toString(address.getPostCode()) : "");

                    hotelDTO.put("address", addressJson);
                    hotelsDTO.put(hotelDTO);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            setResponseErrorMessage("error marshalling rooms data");
        }
        return hotelsDTO;
    }

}
