package lans.hotels.use_cases;
import lans.hotels.api.DTOs.AddressDTO;
import lans.hotels.api.DTOs.HotelGroupDTO;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel_group.HotelGroup;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetHotelGroups extends UseCase {

    ArrayList<HotelGroup> hotel_groups;
    Integer groupId = -1;

    public GetHotelGroups(IDataSource dataSource, Integer groupId) {
        super(dataSource);
        this.hotel_groups = new ArrayList<>();
        this.groupId = groupId;
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
            if (succeeded) jsonArray = createHotelGroupsJSON();
        } catch (Exception e) {
            fail();
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        } finally {
            responseData.put("groups", jsonArray);
        }
    }

    private JSONArray createHotelGroupsJSON() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (hotel_groups != null) {
                hotel_groups.forEach(hotelGroup -> {
                    if (groupId == -1 || groupId.equals(hotelGroup.getId())) {
                        HotelGroupDTO hotelGroupDTO = new HotelGroupDTO(hotelGroup);
                        jsonArray.put(hotelGroupDTO.json());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            setResponseErrorMessage("error marshalling hotel group data");
        }
        return jsonArray;
    }

}
