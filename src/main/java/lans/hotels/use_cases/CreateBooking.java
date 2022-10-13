package lans.hotels.use_cases;

import lans.hotels.api.exceptions.CommandException;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import lans.hotels.domain.utils.DateRange;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class CreateBooking extends UseCase {
    JSONObject bookingJson;
    Booking booking;
    public CreateBooking(IDataSource dataSource, JSONObject bookingJson) {
        super(dataSource);
        this.bookingJson = bookingJson;
    }

    @Override
    public void doExecute() throws Exception {
        booking = createBookingFromJson(bookingJson);
        succeed();
    }

    @Override
    protected void constructResult() {

    }

    private Booking createBookingFromJson(JSONObject bookingJson) throws CommandException {
        System.out.println("createBookingFromJson:\t" + bookingJson);
        Booking booking;
        try {
            Integer customerId = (Integer) bookingJson.get("customer_id");
            Integer hotelId = (Integer) bookingJson.get("hotel_id");
            DateRange dateRange = parseDateRange((String) bookingJson.get("start_date"), (String) bookingJson.get("end_date"));
            HashMap<Integer, RoomBooking> roomBookings = parseRoomBookings(bookingJson.getJSONArray("rooms"));
            booking = new Booking(dataSource, customerId, hotelId, dateRange, roomBookings);
        } catch (Exception e) {
            System.err.println("Booking JSON:\n" + bookingJson);
            e.printStackTrace();
            fail();
            throw new CommandException("internal server error");
        }

        return booking;
    }

    private DateRange parseDateRange(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Date from = Date.valueOf(LocalDate.parse(startDate, formatter));
        Date to = Date.valueOf(LocalDate.parse(endDate, formatter));
        DateRange dateRange = new DateRange(from, to);
        return dateRange;
    }

    private HashMap<Integer, RoomBooking> parseRoomBookings(JSONArray roomsJson) throws UoWException {
        HashMap<Integer, RoomBooking> roomBookings = new HashMap<>();
        for(int i = 0; i < roomsJson.length(); i++) {
            JSONObject roomJson = roomsJson.getJSONObject(i);
            RoomBooking roomBooking;
            try {
                roomBooking = new RoomBooking(dataSource,
                        roomJson.getInt("room_id"),
                        roomJson.getString("main_guest_name"),
                        roomJson.getInt("no_of_guests"));
            } catch (UoWException e) {
                throw e;
            }
            roomBookings.put(roomBooking.getRoomId(), roomBooking);
        }
        return roomBookings;
    }
}
