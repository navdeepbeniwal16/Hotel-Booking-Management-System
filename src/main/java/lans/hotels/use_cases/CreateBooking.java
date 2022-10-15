package lans.hotels.use_cases;

import lans.hotels.api.exceptions.CommandException;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.datasource.search_criteria.RoomBookingSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.utils.DateRange;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class CreateBooking extends UseCase {

    public CreateBooking(IDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void doExecute() throws Exception {
        succeed();
    }

    @Override // TODO? @levimk
    protected void constructResult() {

    }

    private Booking createBookingFromJson(JSONObject bookingJson) {
        Booking booking = null;
        try {

        } catch (Exception e) {
            System.err.println("Booking JSON:\n" + bookingJson);
            e.printStackTrace();
            fail();
        }
        return booking;
    }

}
