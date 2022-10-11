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

    String hotelier_email;
    ArrayList<Booking> bookings;


    public ViewHotelGroupBookings(IDataSource dataSource,String hotelier_email) {
        super(dataSource);
        this.hotelier_email = hotelier_email;
        this.bookings = new ArrayList<>();
    }

    @Override
    public void doExecute() throws Exception {

        ArrayList<User> hoteliers;

        UserSearchCriteria u_criteria = new UserSearchCriteria();
        u_criteria.setEmail(hotelier_email);

        hoteliers = dataSource.findBySearchCriteria(User.class, u_criteria);

        if(hoteliers.size() > 0) {
            User hgh = hoteliers.get(0);

            BookingsSearchCriteria b_criteria = new BookingsSearchCriteria();
            b_criteria.setHotelGroupId(hgh.getHotelierHotelGroupID());
            try {
                bookings = dataSource.findBySearchCriteria(Booking.class, b_criteria);
                succeed();
            } catch (Exception e) {
                fail();
                e.printStackTrace();
                throw e;
            }

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
                b_entry.put("customer_id", booking.getCustomerId());
                b_entry.put("customer_name", booking.getCustomerName());
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
