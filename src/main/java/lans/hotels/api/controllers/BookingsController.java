package lans.hotels.api.controllers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.*;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.user.User;
import lans.hotels.domain.utils.DateRange;
import lans.hotels.use_cases.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BookingsController extends FrontCommand {
    @Override
    protected void concreteProcess() throws Exception {
        System.out.println("BookingsController.concreteProcess(): " + request.getMethod() + " " + request.getRequestURI());
        switch (request.getMethod()) {
            case HttpMethod.GET:
                responseHelper.error("GET /bookings: NOT IMPLEMENTED", HttpServletResponse.SC_NOT_IMPLEMENTED);
                return;
            case HttpMethod.POST:
                asCustomerOrHotelier(this::handlePost);
                return;
            case HttpMethod.PUT:
                asCustomerOrHotelier(this::handlePut);
                return;
            default:
                responseHelper.unimplemented(request.getMethod() + " /bookings");
        }
    }

    public Void handlePost() throws Exception {
        if (requestHelper.body().has("booking")) {
            asCustomer(this::createNewBooking);
        }
        else if(requestHelper.body().has("search"))
        {
            JSONObject searchQuery = requestHelper.body().getJSONObject("search");
            handleSearchQuery(searchQuery);
        }
        else
            responseHelper.error("POST /bookings must contain booking or search",HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    private Void createNewBooking() {

        JSONObject bookingJson = requestHelper.body("booking");
        if(!(bookingJson.has("hotel_id")&&bookingJson.has("start_date")&&bookingJson.has("end_date")&&bookingJson.has("rooms"))){
            responseHelper.error("bookings must contain hotel_id, start_date, end_date, rooms",HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        Booking booking = null;

        Integer customerId = auth.getId();
        Integer hotelId = (Integer) bookingJson.get("hotel_id");

        ArrayList<Hotel> hotels = null;
        HotelSearchCriteria hg_criteria = new HotelSearchCriteria();
        hg_criteria.setId(hotelId);
        try {
            hotels = dataSource.findBySearchCriteria(Hotel.class, hg_criteria);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (hotels.size()==0) {
            responseHelper.error("POST /bookings hotel id does not exist", HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        if(!hotels.get(0).getIsActive())
        {
            responseHelper.error("POST /bookings hotel is not active", HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("here");
            return null;
        }

        long millis=System.currentTimeMillis();
        java.sql.Date today_date=new java.sql.Date(millis);

        DateRange dateRange = parseDateRange((String) bookingJson.get("start_date"), (String) bookingJson.get("end_date"));
        if (dateRange.getFrom().compareTo(dateRange.getTo())>0){
            responseHelper.error("start date less than end date",HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        if (dateRange.getFrom().compareTo(today_date)>=0)
        {
            responseHelper.error("start date less than current date",HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }


        HashMap<Integer, RoomBooking> roomBookings = null;
        try {
            roomBookings = parseRoomBookings(bookingJson.getJSONArray("rooms") , dateRange, hotelId);
            if (roomBookings == null) throw new Exception("invalid room bookings json");
            if(roomBookings.size()>0) {
                booking = new Booking(dataSource, customerId, hotelId, dateRange, roomBookings);
                useCase = new CreateBooking(dataSource);
                useCase.execute();
                responseHelper.respond(useCase.getResult(), HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseHelper.error(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    private HashMap<Integer, RoomBooking> parseRoomBookings(JSONArray roomsJson,DateRange date_range,Integer hotel_id) throws Exception {
        HashMap<Integer, RoomBooking> roomBookings = new HashMap<>();
        System.out.println("---ROOMS JSON---\n" + roomsJson);
        for(int i = 0; i < roomsJson.length(); i++) {
            JSONObject roomJson = roomsJson.getJSONObject(i);
            RoomBooking roomBooking = null;
            try {
                if(roomJson.has("id") &&
                        roomJson.has("no_of_guests") &&
                        roomJson.has("main_guest_name")){


                    ArrayList<RoomBooking> rbs = null;
                    RoomBookingSearchCriteria r_criteria = new RoomBookingSearchCriteria();
                    r_criteria.setDate_range(date_range);
                    r_criteria.setRoomID(roomJson.getInt("id"));
                    try {
                        rbs = dataSource.findBySearchCriteria(RoomBooking.class, r_criteria);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (rbs.size()>0){
                        throw new Exception("room booking dates overlap with existing booking");
                    }

                    roomBooking = new RoomBooking(dataSource,
                            roomJson.getInt("id"),
                            roomJson.getString("main_guest_name"),
                            roomJson.getInt("no_of_guests"));

                }
                else{
                    throw new Exception("room bookings must contain room: { id: number, no_of_guests: number, main_guest_name: string}, received\n" + roomJson.toString());
                }
            } catch (Exception e) {
                throw e;
            }
            roomBookings.put(roomBooking.getRoomId(), roomBooking);
        }
        return roomBookings;
    }

    private DateRange parseDateRange(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Date from = Date.valueOf(LocalDate.parse(startDate, formatter));
        Date to = Date.valueOf(LocalDate.parse(endDate, formatter));
        DateRange dateRange = new DateRange(from, to);
        return dateRange;
    }

    private void handleSearchQuery(JSONObject searchQuery) {
        if(searchQuery.has("hotel_id")) {
            Integer hotel_id = searchQuery.getInt("hotel_id");

            if(!checkHotelGroupHotelierValid(hotel_id))
            {
                responseHelper.unauthorized();
                return;
            }

            useCase = new ViewHotelGroupBookings(dataSource, hotel_id);
            useCase.execute();
            statusCode = useCase.succeeded() ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            responseHelper.respond(useCase.getResult(), statusCode);
        }

        else if(searchQuery.has("customer_bookings")) {
            Integer customer_id = auth.getId();
            useCase = new ViewCustomerBookings(dataSource, customer_id);
            useCase.execute();
            statusCode = useCase.succeeded() ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            responseHelper.respond(useCase.getResult(), statusCode);
        }
        else
            responseHelper.error("POST /bookings search must contain hotel_id or customer_bookings",HttpServletResponse.SC_BAD_REQUEST);
    }

    private boolean checkHotelGroupHotelierValid(Integer h_id){
        Integer hotelier_hg_id = -1;
        Integer hotel_hg_id =-2;
        String hotelier_email = auth.getEmail();

        ArrayList<User> hoteliers = null;

        UserSearchCriteria u_criteria = new UserSearchCriteria();
        u_criteria.setEmail(hotelier_email);

        try {
            hoteliers = dataSource.findBySearchCriteria(User.class, u_criteria);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(hoteliers.size() > 0) {
            User hgh = hoteliers.get(0);
            hotelier_hg_id = hgh.getHotelierHotelGroupID();
        }
        ArrayList<Hotel> hotels = null;

        HotelSearchCriteria h_criteria = new HotelSearchCriteria();
        h_criteria.setId(h_id);

        try {
            hotels = dataSource.findBySearchCriteria(Hotel.class, h_criteria);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(hotels.size() > 0) {
            Hotel h = hotels.get(0);
            hotel_hg_id = h.getHotelGroupID();
        }

        return hotelier_hg_id == hotel_hg_id;
    }

    public Void handlePut() throws Exception {
        if(requestHelper.body().has("booking")) {
            JSONObject bookingJsonBody = requestHelper.body().getJSONObject("booking");

            if (!bookingJsonBody.has("version")) {
                responseHelper.error("Updating a booking must include a version number", HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
            Integer version = bookingJsonBody.getInt("version");

            if(bookingJsonBody.has("id")) {
                Integer bookingId = bookingJsonBody.getInt("id");




                if(bookingId != null) {
                    ArrayList<Booking> bookings = null;
                    Booking booking = null;
                    BookingsSearchCriteria criteria = new BookingsSearchCriteria();
                    criteria.setBookingId(bookingId);
                    try {
                        bookings = dataSource.findBySearchCriteria(Booking.class, criteria);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(bookings.size() > 0)
                        booking = bookings.get(0);
                    else {
                        responseHelper.error("Booking does not exist", HttpServletResponse.SC_BAD_REQUEST);
                        return null;
                    }
                    if(!booking.getActive()) {
                        responseHelper.error("Booking cancelled.", HttpServletResponse.SC_BAD_REQUEST);
                        return null;
                    }

                    if (version != booking.getVersion()) {
                        responseHelper.error("Attempting to update booking using outdated version", HttpServletResponse.SC_CONFLICT);
                        return null;
                    }

                    if(bookingJsonBody.has("cancel")) {
                        handleCancelBooking(booking);
                    }
                    else if(bookingJsonBody.has("start_date") && bookingJsonBody.has("end_date")) {
                        handleDateChange(bookingJsonBody,booking);
                    }
                    else
                        responseHelper.error("Booking search must contain cancel, or start date and end_date fields",HttpServletResponse.SC_BAD_REQUEST);
                }
            }
            else
                responseHelper.error("Booking search must contain id field",HttpServletResponse.SC_BAD_REQUEST);
        }
        else
            responseHelper.error("Booking search must contain booking field",HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    private void handleCancelBooking(Booking booking) throws Exception {

        if (auth.isCustomer()) {
            if (booking.getCustomerId() != auth.getId()){
                responseHelper.unauthorized();
                return;
            }
        }
        else if (auth.isHotelier()) {
            ArrayList<Hotel> hotels = null;

            HotelSearchCriteria h_criteria = new HotelSearchCriteria();
            h_criteria.setId(booking.getHotelId());

            try {
                hotels = dataSource.findBySearchCriteria(Hotel.class, h_criteria);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Hotel h = null;
            if (hotels.size() > 0) {
                h = hotels.get(0);
            }
            if (auth.getUser().getHotelierHotelGroupID() != h.getHotelGroupID()){
                responseHelper.unauthorized();
                return;
            }

        }
        else
        {
            responseHelper.unauthorized();
            return;
        }


        useCase = new CancelBooking(dataSource,booking);
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responseHelper.respond(useCase.getResult(), statusCode);
    }

    public void handleDateChange(JSONObject bookingJsonBody, Booking booking) throws Exception {
        if (auth.isCustomer()) {
            if (booking.getCustomerId() != auth.getId()){
                responseHelper.unauthorized();
                return;
            }
        }

        String startDateString = bookingJsonBody.getString("start_date");
        String endDateString = bookingJsonBody.getString("end_date");
        java.sql.Date startDate = new Date(dateFormat.parse(startDateString).getTime());
        java.sql.Date endDate = new Date(dateFormat.parse(endDateString).getTime());

        long millis=System.currentTimeMillis();
        java.sql.Date today_date=new java.sql.Date(millis);

        DateRange dateRange = parseDateRange((String) bookingJsonBody.getString("start_date"), bookingJsonBody.getString("end_date"));
        if (dateRange.getFrom().compareTo(dateRange.getTo())>0){
            responseHelper.error("start date less than end date",HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (dateRange.getFrom().compareTo(today_date)>=0)
        {
            responseHelper.error("start date less than current date",HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Hotel hotel = null;
        HotelSearchCriteria hotelSearchCriteria = new HotelSearchCriteria();
        hotelSearchCriteria.setId(booking.getHotelId());
        try {
            ArrayList<Hotel> hotels = dataSource.findBySearchCriteria(Hotel.class, hotelSearchCriteria);
            if(hotels.size() < 1) throw new RuntimeException("No hotels found for the booking...");

            hotel = hotels.get(0);

            if(!hotels.get(0).getIsActive())
            {
                responseHelper.error("hotel is delisted",HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
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
            responseHelper.error("booking dates requested clash with existing bookings",HttpServletResponse.SC_BAD_REQUEST);
            return;
        } else {
            booking.setDateRange(dateRange);
        }

        useCase = new ChangeBookingDates(dataSource);
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responseHelper.respond(useCase.getResult(), statusCode);

    }
}
