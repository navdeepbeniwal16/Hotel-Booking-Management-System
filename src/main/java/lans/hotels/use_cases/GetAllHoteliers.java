package lans.hotels.use_cases;

import lans.hotels.datasource.search_criteria.UserSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user_types.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetAllHoteliers extends UseCase {
    ArrayList<User> users;
    UserSearchCriteria criteria;

    public GetAllHoteliers(IDataSource dataSource, UserSearchCriteria criteria) {
        super(dataSource);
        this.criteria = criteria;
        this.users = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {
        try {
            users = dataSource.findBySearchCriteria(User.class, criteria);
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
                jsonArray = createUsersJson();
            }
        } catch (Exception e) {
            fail();
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        } finally {
            responseData.put("users", jsonArray);
        }
    }

    private JSONArray createUsersJson() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (users == null) {
                System.err.println("null users");
            }
            users.forEach(user -> {
                JSONObject userJson;
                userJson = new JSONObject();
                userJson.put("id", user.getID());
                userJson.put("role", user.getRoleId());
                userJson.put("name", user.getName());
                userJson.put("email", user.getEmail());
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
