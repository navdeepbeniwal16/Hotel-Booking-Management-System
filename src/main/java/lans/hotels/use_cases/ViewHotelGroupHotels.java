package lans.hotels.use_cases;

import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.datasource.search_criteria.UserSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.user_types.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewHotelGroupHotels extends UseCase {

    String hotelier_email;
    ArrayList<Hotel> hotels;


    public ViewHotelGroupHotels(IDataSource dataSource,String hotelier_email) {
        super(dataSource);
        this.hotelier_email = hotelier_email;
        this.hotels = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {

        ArrayList<User> hoteliers;

        UserSearchCriteria u_criteria = new UserSearchCriteria();
        u_criteria.setEmail(hotelier_email);

        hoteliers = dataSource.findBySearchCriteria(User.class, u_criteria);

        if(hoteliers.size() > 0) {
            User hgh = hoteliers.get(0);

            HotelSearchCriteria h_criteria = new HotelSearchCriteria();
            h_criteria.setHotelGroupId(hgh.getHotelierHotelGroupID());
            try {
                hotels = dataSource.findBySearchCriteria(Hotel.class, h_criteria);
                succeed();
            } catch (Exception e) {
                fail();
                e.printStackTrace();
                throw e;
            }

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
            responseData.put("hotels", jsonArray);
        }
    }

    private JSONArray createHotelGroupsJSON() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (hotels == null) {
                System.err.println("null bookings");
            }
            hotels.forEach(hotel -> {
                JSONObject b_entry;
                b_entry = new JSONObject();
                b_entry.put("id", hotel.getId());
                b_entry.put("hotel_name", hotel.getName());
                b_entry.put("hotel_email", hotel.getEmail());
                b_entry.put("contact", hotel.getContact());
                b_entry.put("city", hotel.getCity());
                b_entry.put("pin_code", hotel.getPinCode());
                b_entry.put("is_active", hotel.getIsActive());
                jsonArray.put(b_entry);
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            setResponseErrorMessage("error marshalling rooms data");
        }
        return jsonArray;
    }

}
