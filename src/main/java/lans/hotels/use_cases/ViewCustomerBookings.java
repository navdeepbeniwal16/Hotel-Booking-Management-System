package lans.hotels.use_cases;

import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewCustomerBookings extends UseCase {

    Integer customer_id;
    ArrayList<Booking> bookings;


    public ViewCustomerBookings(IDataSource dataSource,Integer customer_id) {
        super(dataSource);
        this.customer_id = customer_id;
        this.bookings = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {

        BookingsSearchCriteria b_criteria= new BookingsSearchCriteria();
        b_criteria.setCustomerId(customer_id);
        try {
            bookings = dataSource.findBySearchCriteria(Booking.class,b_criteria);
            bookings.forEach(Booking::getRoomBookings);
            succeed();
        } catch (Exception e) {
            fail();
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    protected void constructResult() {
        try {
            if (succeeded) responseData.put("bookings", createHotelGroupsJSON());
        } catch (Exception e) {
            fail();
            e.printStackTrace();
            setResponseErrorMessage("error building respond object");
        }
    }

    private JSONArray createHotelGroupsJSON() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (bookings == null) {
                System.err.println("null bookings");
            }
            bookings.forEach(booking -> {
                JSONObject b_entry;
                b_entry = new JSONObject();
                b_entry.put("id", booking.getId());
                b_entry.put("hotel_id", booking.getHotelId());
                b_entry.put("hotel_name", booking.getHotelName());
                b_entry.put("start_date", booking.getDateRange().getFrom().toString());
                b_entry.put("end_date", booking.getDateRange().getTo().toString());
                b_entry.put("is_active", booking.getActive());
                JSONArray rooms = new JSONArray();
                booking.getRoomBookings().values().forEach(roomBooking -> {
                    JSONObject room = new JSONObject();
                    room.put("main_guest", roomBooking.getMainGuest());
                    room.put("guests", roomBooking.getNumOfGuests());
                    rooms.put(room);
                });
                b_entry.put("rooms", rooms);
                jsonArray.put(b_entry);
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            setResponseErrorMessage("error marshalling hotel group data");
        }
        return jsonArray;
    }

}
