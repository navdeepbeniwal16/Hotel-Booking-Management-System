package lans.hotels.use_cases;

import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewRoomBookingsForBooking extends UseCase {
    Integer bookingId;
    Booking booking = null;
    HashMap<Integer, RoomBooking> roomBookings = new HashMap<>();

    public ViewRoomBookingsForBooking(IDataSource dataSource) {
        super(dataSource);
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public void doExecute() throws Exception {
        try {
            BookingsSearchCriteria bookingsSearchCriteria = new BookingsSearchCriteria();
            bookingsSearchCriteria.setBookingId(bookingId);
            ArrayList<Booking> bookings = dataSource.findBySearchCriteria(Booking.class, bookingsSearchCriteria);
            if(bookings.size() > 0) booking = bookings.get(0);
            if(booking != null) { roomBookings = booking.getRoomBookings(); }
            succeed();
        } catch (Exception e) {
            fail();
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    protected void constructResult() {
        JSONArray results = new JSONArray();

        try {
            if (succeeded) {
                for(RoomBooking roomBooking: roomBookings.values()) {
                    JSONObject roomBookingJson = new JSONObject();
                    roomBookingJson.put("id", roomBooking.getId());
                    roomBookingJson.put("room_id", roomBooking.getRoomId());
                    roomBookingJson.put("main_guest", roomBooking.getMainGuest());
                    roomBookingJson.put("number_of_guests", roomBooking.getNumOfGuests());
                    results.put(roomBookingJson);
                }
            }
        } catch (Exception e) {
            fail();
            e.printStackTrace();
            setResponseErrorMessage("Server Error: " + e.getMessage());
        } finally {
            succeeded();
            responseData.put("room_bookings", results);
        }
    }
}
