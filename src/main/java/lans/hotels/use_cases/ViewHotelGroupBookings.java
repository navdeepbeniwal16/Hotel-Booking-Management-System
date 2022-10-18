package lans.hotels.use_cases;

import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.datasource.search_criteria.UserSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.user.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewHotelGroupBookings extends UseCase {

    Integer h_id;
    ArrayList<Booking> bookings;

    public ViewHotelGroupBookings(IDataSource dataSource,Integer h_id) {
        super(dataSource);
        this.h_id = h_id;
        this.bookings = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {
        BookingsSearchCriteria b_criteria = new BookingsSearchCriteria();
        b_criteria.setHotelId(h_id);
        try {
            bookings = dataSource.findBySearchCriteria(Booking.class, b_criteria);
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
            setResponseErrorMessage("error marshalling result");
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
                b_entry.put("customer_id", booking.getCustomerId());
                b_entry.put("customer_name", booking.getCustomerName());
                b_entry.put("hotel_id", booking.getHotelId());
                b_entry.put("hotel_name", booking.getHotelName());
                b_entry.put("start_date", booking.getDateRange().getFrom().toString());
                b_entry.put("end_date", booking.getDateRange().getTo().toString());
                b_entry.put("is_active", booking.getActive());
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
