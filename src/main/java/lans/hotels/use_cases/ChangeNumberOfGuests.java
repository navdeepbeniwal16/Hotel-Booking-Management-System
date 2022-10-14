package lans.hotels.use_cases;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangeNumberOfGuests extends UseCase {

    Booking booking;
    Integer rb_id;
    Integer no_of_guests;

    public ChangeNumberOfGuests(IDataSource dataSource, Booking booking, Integer rb_id, Integer no_of_guests) {
        super(dataSource);
        this.booking = booking;
        this.rb_id = rb_id;
        this.no_of_guests = no_of_guests;
    }

    @Override
    public void doExecute() throws Exception {
        HashMap<Integer, RoomBooking> rBookings = booking.getRoomBookings();
        RoomBooking rBooking = rBookings.get(rb_id);
        rBooking.setNumOfGuests(no_of_guests);
        succeed();
    }

    @Override
    protected void constructResult() {
        JSONObject resposeObject = new JSONObject();
        try {
            if (succeeded) {
                resposeObject.put("cancelled", succeeded);
            }
        } catch (Exception e) {
            resposeObject.put("cancelled", succeeded);
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        }
    }


}
