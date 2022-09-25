package lans.hotels.use_cases;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user_types.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetAllUsers extends UseCase {
    ArrayList<User> users;

    public GetAllUsers(IDataSource dataSource) {
        super(dataSource);
        this.users = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {
        try {
            users = dataSource.findAll(User.class);
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
            setResponseErrorMessage("error marshalling user data");
        }
        return jsonArray;
    }
}
