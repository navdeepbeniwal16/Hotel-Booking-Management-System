package lans.hotels.api.controllers;

import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.datasource.search_criteria.UserSearchCriteria;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.user.User;
import lans.hotels.domain.utils.DateRange;
import lans.hotels.use_cases.CreateBooking;
import lans.hotels.use_cases.ViewCustomerBookings;
import lans.hotels.use_cases.ViewHotelGroupBookings;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
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

                // PUT: api/bookings
                if(requestHelper.body().has("booking")) {
                    JSONObject bookingJsonBody = requestHelper.body().getJSONObject("booking");

                    if(!bookingJsonBody.has("id")) {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                        return;
                    } else {
                        Integer bookingId = bookingJsonBody.getInt("id");

                        if(bookingId != null) {
                            // fetching booking object from the database
                            BookingsSearchCriteria criteria = new BookingsSearchCriteria();
                            criteria.setBookingId(bookingId);
                            try {
                                ArrayList<Booking> bookings = dataSource.findBySearchCriteria(Booking.class, criteria);
                                if(bookings.size() > 0) {
                                    Booking booking = bookings.get(0);

                                    // Cancelling room bookings as well
                                    if(bookingJsonBody.has("isActive") && !bookingJsonBody.getBoolean("isActive") && booking.getActive()) {
                                        HashMap<Integer, RoomBooking> roomBookingHashMap = booking.getRoomBookings();
                                        for(Object roomBookingKey: roomBookingHashMap.keySet()) {
                                            roomBookingHashMap.get(roomBookingKey).setActive(false);
                                        }
                                        booking.setActive(false);
                                    }

                                    if(bookingJsonBody.has("room_bookings") && bookingJsonBody.getJSONArray("room_bookings").length() > 0) {
                                        JSONArray roomBookingsArray = bookingJsonBody.getJSONArray("room_bookings");
                                        for(int rbIndex=0; rbIndex < roomBookingsArray.length(); rbIndex++) {
                                            JSONObject rbObject = roomBookingsArray.getJSONObject(rbIndex);
                                            HashMap<Integer, RoomBooking> rBookings = booking.getRoomBookings();
                                            for(Object rbKey: rBookings.keySet()) {
                                                RoomBooking rBooking = rBookings.get(rbKey);

                                                System.out.println("Reaching here..."); // TODO: TBR
                                                if(rbObject.has("id") && rbObject.getInt("id") == rBooking.getId()) {
                                                    if(rbObject.has("no_of_guests") && rbObject.getInt("no_of_guests") != rBooking.getNumOfGuests()) {
                                                        rBooking.setNumOfGuests(rbObject.getInt("no_of_guests"));
                                                    }
                                                    // More updates to be added here

                                                }
                                            }
                                        }
                                    }

                                    if(bookingJsonBody.has("start_date") && bookingJsonBody.has("end_date")) {
                                        java.sql.Date startDate;
                                        java.sql.Date endDate;
                                        try {
                                            String startDateString = bookingJsonBody.getString("start_date");
                                            String endDateString = bookingJsonBody.getString("end_date");
                                            startDate = new Date(dateFormat.parse(startDateString).getTime());
                                            endDate = new Date(dateFormat.parse(endDateString).getTime());
                                        } catch (Exception e){
                                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                                            return;
                                        }

                                        Hotel hotel;
                                        HotelSearchCriteria hotelSearchCriteria = new HotelSearchCriteria();
                                        hotelSearchCriteria.setId(booking.getHotelId());
                                        try {
                                            ArrayList<Hotel> hotels = dataSource.findBySearchCriteria(Hotel.class, hotelSearchCriteria);
                                            if(hotels.size() < 1) throw new RuntimeException("No hotels found for the booking...");

                                            hotel = hotels.get(0);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            return;
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
                                            responseHelper.error("Room Bookings are clashing",400);
                                            return;
                                        } else {
                                            DateRange dateRange = new DateRange(startDate, endDate);
                                            booking.setDateRange(dateRange);
                                        }
                                    }

                                    dataSource.commit();

                                } else {
                                    response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
                                    return;
                                }

                            } catch (Exception e) {
                                System.err.println("PUT /api/bookings: " + e.getMessage());
                                System.err.println("PUT /api/bookings: " + e.getClass());
                                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                                e.printStackTrace();
                                return;
                            }
                        }
                    }
                }
                return;
            default:
                responseHelper.unimplemented(request.getMethod() + " /bookings");
        }
    }

    public Void handlePost() throws Exception {
        if (requestHelper.body().has("booking")) {
            handleCreateNewBooking();
        }
        else if(requestHelper.body().has("search"))
        {
            JSONObject searchQuery = requestHelper.body().getJSONObject("search");
            handleSearchQuery(searchQuery);
        }
        return null;
    }

    private void handleCreateNewBooking() {
        try {
            auth.withGuard(this::createNewBookingGuard, this::createNewBooking);
        } catch (Exception e) {
            e.printStackTrace();
            responseHelper.internalServerError();
        }
    }

    private Boolean createNewBookingGuard() {
        Integer hotelId = (Integer) requestHelper.body("booking", "hotel_id");
        boolean authCheck = Boolean.FALSE;
        if (auth.isHotelier()) {
            authCheck = hotelId.equals(auth.getUser().getHotelierHotelGroupID());
        }
        else return true;
        return authCheck;
    }

    private Void createNewBooking() {

        useCase = new CreateBooking(dataSource, requestHelper.body("booking"), auth.getId());
        useCase.execute();
        responseHelper.respond(useCase.getResult(), HttpServletResponse.SC_OK);
        return null;
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

        if(searchQuery.has("customer_bookings")) {
            Integer customer_id = auth.getId();
            useCase = new ViewCustomerBookings(dataSource, customer_id);
            useCase.execute();
            statusCode = useCase.succeeded() ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            responseHelper.respond(useCase.getResult(), statusCode);
        }
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
        if(hotelier_hg_id != hotel_hg_id){
            responseHelper.unauthorized();
            return false;
        }
        else return true;
    }

}
