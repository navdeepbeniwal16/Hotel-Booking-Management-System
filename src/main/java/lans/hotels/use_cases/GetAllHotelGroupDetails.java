package lans.hotels.use_cases;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel_group.HotelGroup;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetAllHotelGroupDetails extends UseCase {

    ArrayList<HotelGroup> hotel_groups;

    public GetAllHotelGroupDetails(IDataSource dataSource) {
        super(dataSource);
        this.hotel_groups = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {

        try {
            hotel_groups = dataSource.findAll(HotelGroup.class);
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
                System.err.println("null hoteliers");
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
