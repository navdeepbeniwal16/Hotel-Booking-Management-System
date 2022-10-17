package lans.hotels.use_cases;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangeNumberOfGuests extends UseCase {

    public ChangeNumberOfGuests(IDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void doExecute() throws Exception {
        succeed();
    }

    @Override
    protected void constructResult() {
        JSONObject resposeObject = new JSONObject();
        try {
            if (succeeded) {
                resposeObject.put("guests_changed", succeeded);
            }
        } catch (Exception e) {
            resposeObject.put("guests_changed", succeeded);
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        }
    }


}
