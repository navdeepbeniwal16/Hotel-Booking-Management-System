package lans.hotels.use_cases;
import lans.hotels.datasource.search_criteria.RoomSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;
import lans.hotels.use_cases.UseCase;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddRoomToHotel extends UseCase {

    public AddRoomToHotel(IDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void doExecute() throws Exception {
        succeed();
    }

    @Override
    protected void constructResult() {
        JSONObject aHG = new JSONObject();
        try {
            if (succeeded) {
                aHG.put("created", succeeded);
            }
        } catch (Exception e) {
            aHG.put("created", succeeded);
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        }
    }

}
