package lans.hotels.use_cases;

import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewCustomerBookings extends UseCase {

    String customer_email;
    ArrayList<Booking> bookings;


    public ViewCustomerBookings(IDataSource dataSource,String  customer_email) {
        super(dataSource);
        this.customer_email = customer_email;
        this.bookings = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {

        BookingsSearchCriteria b_criteria= new BookingsSearchCriteria();
        b_criteria.setCustomerName(customer_email);
        try {
            bookings = dataSource.findBySearchCriteria(Booking.class,b_criteria);
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
            responseData.put("bookings", jsonArray);
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
                b_entry.put("end_date", booking.getDateRange().getFrom().toString());
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
