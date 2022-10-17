package lans.hotels.use_cases;

import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.utils.DateRange;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ChangeBookingDates extends UseCase {

    public ChangeBookingDates(IDataSource dataSource) {
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
                resposeObject.put("changed", succeeded);
            }
        } catch (Exception e) {
            resposeObject.put("changed", succeeded);
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        }
    }


}
