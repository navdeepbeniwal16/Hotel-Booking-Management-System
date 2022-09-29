package lans.hotels.use_cases;
import lans.hotels.datasource.search_criteria.UserSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user_types.Role;
import lans.hotels.domain.user_types.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetAllHoteliers extends UseCase {
    ArrayList<User> hotleiers;

    public GetAllHoteliers(IDataSource dataSource) {
        super(dataSource);
        this.hotleiers = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {

        UserSearchCriteria u_criteria= new UserSearchCriteria();

        Role r = new Role(2);
        u_criteria.setRole(r);

        try {
            hotleiers = dataSource.findBySearchCriteria(User.class, u_criteria);
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
                jsonArray = createHoteliersJSON();
            }
        } catch (Exception e) {
            fail();
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        } finally {
            responseData.put("hoteliers", jsonArray);
        }
    }

    private JSONArray createHoteliersJSON() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (hotleiers == null) {
                System.err.println("null hoteliers");
            }
            hotleiers.forEach(hotelier -> {
                JSONObject userJson;
                userJson = new JSONObject();
                userJson.put("id", hotelier.getID());
                userJson.put("name", hotelier.getName());
                userJson.put("email", hotelier.getEmail());
                userJson.put("role", hotelier.getRole().getName());
                JSONObject hotelGroupJson = new JSONObject();
                hotelGroupJson.put("name", hotelier.getHotelierHotelGroupName());
                hotelGroupJson.put("id", hotelier.getHotelierHotelGroupID());
                userJson.put("hotel_group", hotelGroupJson);
                jsonArray.put(userJson);
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            setResponseErrorMessage("error marshalling hotelier data");
        }
        return jsonArray;
    }
}
