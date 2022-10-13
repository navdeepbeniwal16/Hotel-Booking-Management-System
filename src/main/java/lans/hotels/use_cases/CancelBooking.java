package lans.hotels.use_cases;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import org.json.JSONObject;
import java.util.HashMap;

public class CancelBooking extends UseCase {

    Booking booking;

    public CancelBooking(IDataSource dataSource, Booking booking) {
        super(dataSource);
        this.booking = booking;
    }

    @Override
    public void doExecute() throws Exception {
        HashMap<Integer, RoomBooking> roomBookingHashMap = booking.getRoomBookings();
        for (Object roomBookingKey : roomBookingHashMap.keySet()) {
            roomBookingHashMap.get(roomBookingKey).remove();
        }
        booking.setActive(false);
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
