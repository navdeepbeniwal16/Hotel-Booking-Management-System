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

    Booking booking;
    java.sql.Date startDate;
    java.sql.Date endDate;

    public ChangeBookingDates(IDataSource dataSource, Booking booking, java.sql.Date startDate, java.sql.Date endDate) {
        super(dataSource);
        this.booking = booking;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public void doExecute() throws Exception {
        Hotel hotel = null;
        HotelSearchCriteria hotelSearchCriteria = new HotelSearchCriteria();
        hotelSearchCriteria.setId(booking.getHotelId());
        try {
            ArrayList<Hotel> hotels = dataSource.findBySearchCriteria(Hotel.class, hotelSearchCriteria);
            if(hotels.size() < 1) throw new RuntimeException("No hotels found for the booking...");

            hotel = hotels.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Booking> existingBookings = hotel.getBookings(startDate, endDate);
        HashMap<Integer, RoomBooking> customerRoomBookings = booking.getRoomBookings();
        boolean isRoomBookingClashing = false;
        for (Booking eBooking: existingBookings) {
            if(eBooking.getId() == booking.getId()) continue;

            HashMap<Integer, RoomBooking> existingRoomBookings = eBooking.getRoomBookings();
            for(Object eRoomBookingKey: existingRoomBookings.keySet()) {
                RoomBooking existingRoomBooking = existingRoomBookings.get(eRoomBookingKey);
                for(Object cRoomBookingKey: customerRoomBookings.keySet()) {
                    RoomBooking customerRoomBooking = customerRoomBookings.get(cRoomBookingKey);
                    if(existingRoomBooking.getRoomId() == customerRoomBooking.getRoomId()) {
                        isRoomBookingClashing = true;
                        break;
                    }
                }
            }
        }

        if(isRoomBookingClashing) {
            fail();
        } else {
            System.out.println("Check point 5");
            DateRange dateRange = new DateRange(startDate, endDate);
            booking.setDateRange(dateRange);
            succeed();
        }
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
