package lans.hotels.use_cases;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel_group.HotelGroupHotelier;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetAllHoteliers extends UseCase {
    ArrayList<HotelGroupHotelier> hotleiers;

    public GetAllHoteliers(IDataSource dataSource) {
        super(dataSource);
        this.hotleiers = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {
        try {
            hotleiers = dataSource.findAll(HotelGroupHotelier.class);
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
                userJson.put("hotel_group", hotelier.getHotelGroup());
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
