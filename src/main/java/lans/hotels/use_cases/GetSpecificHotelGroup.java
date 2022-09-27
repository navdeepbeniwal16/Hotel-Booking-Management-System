package lans.hotels.use_cases;

import lans.hotels.datasource.search_criteria.HotelGroupSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.datasource.search_criteria.UserSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.user_types.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetSpecificHotelGroup extends UseCase {

    Integer hotel_group_id;
    ArrayList<User> hoteliers;
    ArrayList<Hotel> hotels;
    ArrayList<HotelGroup> hotel_groups;

    public GetSpecificHotelGroup(IDataSource dataSource,Integer hotel_group_id) {
        super(dataSource);
        this.hotel_group_id = hotel_group_id;
        this.hoteliers = new ArrayList<>();
        this.hotels = new ArrayList<>();
        this.hotel_groups = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {

        HotelGroupSearchCriteria  hotel_group_criteria= new HotelGroupSearchCriteria();
        hotel_group_criteria.setHotelGroupID(hotel_group_id);

        UserSearchCriteria u_criteria= new UserSearchCriteria();
        u_criteria.setHotelierHotelGroupID(hotel_group_id);

        HotelSearchCriteria hotel_criteria = new HotelSearchCriteria();
        hotel_criteria.setHotelGroupId(hotel_group_id);
        try {
            hotel_groups = dataSource.findBySearchCriteria(HotelGroup.class,hotel_group_criteria);
            hoteliers = dataSource.findBySearchCriteria(User.class,u_criteria);
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
            responseData.put("success",true);
            responseData.put("hotel_groups", jsonArray);
        }
    }

    private JSONArray createHotelGroupsJSON() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (hotel_groups == null) {
                System.err.println("null hotel groups");
            }
            hotel_groups.forEach(hotel_group -> {
                JSONObject hg_entry;
                hg_entry = new JSONObject();
                hg_entry.put("id", hotel_group.getId());
                hg_entry.put("name", hotel_group.getName());
                hg_entry.put("phone", hotel_group.getPhone());
                    JSONObject address = new JSONObject();
                    address.put("line_1",hotel_group.getAddress().getLine1());
                    address.put("line_2",hotel_group.getAddress().getLine2());
                    address.put("district",hotel_group.getAddress().getDistrict().toString());
                    address.put("city",hotel_group.getAddress().getCity());
                    address.put("postcode",hotel_group.getAddress().getPostCode());
                hg_entry.put("address",address);

                    JSONArray hotels_array = new JSONArray();
                    hotels.forEach(hotel -> {
                        JSONObject hotel_entry = new JSONObject();
                        hotel_entry.put("hotel_group_id", hotel.getHotelGroupID());
                        hotel_entry.put("name", hotel.getName());
                        hotel_entry.put("hotel_id", hotel.getID());
                        hotel_entry.put("email", hotel.getEmail());
                        hotel_entry.put("address",(hotel.getAddress().getLine1()+", "+hotel.getAddress().getLine2()
                        +" ,"+hotel.getAddress().getCity()+" ,"+hotel.getAddress().getPostCode()));
                        hotel_entry.put("district",hotel.getAddress().getDistrict().toString());
                        hotel_entry.put("contact", hotel.getContact());
                        hotel_entry.put("city", hotel.getCity());
                        hotel_entry.put("pincode", hotel.getPinCode());
                        hotel_entry.put("is_active", hotel.getIsActive());
                        hotels_array.put(hotel_entry);
                    });
                    hg_entry.put("hotels",hotels_array);

                    JSONArray hoteliers_array = new JSONArray();
                    hoteliers.forEach(hotelier -> {
                        JSONObject hotelier_entery = new JSONObject();
                        hotelier_entery.put("id", hotelier.getID());
                        hotelier_entery.put("name", hotelier.getName());
                        hotelier_entery.put("email", hotelier.getEmail());
                        hotelier_entery.put("role", hotelier.getRole().getName());
                        hoteliers_array.put(hotelier_entery);
                    });
                    hg_entry.put("hoteliers",hoteliers_array);

                jsonArray.put(hg_entry);
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            setResponseErrorMessage("error marshalling hotel group data");
        }
        return jsonArray;
    }

}
