package lans.hotels.api.controllers;

import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.utils.DateRange;
import lans.hotels.use_cases.CreateBooking;
import lans.hotels.use_cases.ViewCustomerBookings;
import lans.hotels.use_cases.ViewHotelGroupBookings;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.*;

public class BookingsController extends FrontCommand {
    @Override
    protected void concreteProcess() throws IOException {
        String[] commandPath = request.getPathInfo().split("/");
        int statusCode;
        switch (request.getMethod()) {
            case HttpMethod.GET:
            case HttpMethod.POST:
                if (requestHelper.body().has("booking")) {
                    handleCreateNewBooking();
                    break;
                }
            {

                if(requestHelper.body().has("search"))
                {
                    JSONObject searchQuery = requestHelper.body().getJSONObject("search");

                    if(searchQuery.has("hotelier_email")) {
                        String hotelier_email = searchQuery.getString("hotelier_email");
                        if (!auth.isHotelier()) {
                            responseHelper.unauthorized();
                            return;
                        }
                        useCase = new ViewHotelGroupBookings(dataSource, hotelier_email);
                        useCase.execute();
                        statusCode = useCase.succeeded() ?
                                HttpServletResponse.SC_OK :
                                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                        responseHelper.respond(useCase.getResult(), statusCode);
                        return;
                    }

                    if(searchQuery.has("customer_email")) {
                        String customer_email = searchQuery.getString("customer_email");
                        if (!auth.isCustomer()) {
                            responseHelper.unauthorized();
                            return;
                        }
                        useCase = new ViewCustomerBookings(dataSource, customer_email);
                        useCase.execute();
                        statusCode = useCase.succeeded() ?
                                HttpServletResponse.SC_OK :
                                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                        responseHelper.respond(useCase.getResult(), statusCode);
                        return;
                    }
                }
            }

                break;
            case HttpMethod.PUT:

                // PUT: api/bookings
                if(commandPath.length == 2) {
                    if(requestHelper.body().has("booking")) {
                        JSONObject bookingJsonBody = requestHelper.body().getJSONObject("booking");

                        if(!bookingJsonBody.has("id")) {
                            System.err.println("GET /api/bookings: " + Arrays.toString(commandPath));
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
                                                System.err.println("PUT /api/bookings: " + Arrays.toString(commandPath));
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
                                                System.err.println("PUT /api/bookings: " + Arrays.toString(commandPath));
                                                responseHelper.error("Room Bookings are clashing",400);
                                                return;
                                            } else {
                                                DateRange dateRange = new DateRange(startDate, endDate);
                                                booking.setDateRange(dateRange);
                                            }
                                        }

                                        dataSource.commit();

                                    } else {
                                        System.err.println("PUT /api/bookings: " + Arrays.toString(commandPath));
                                        response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
                                        return;
                                    }

                                } catch (Exception e) {
                                    System.err.println("PUT /api/bookings: " + Arrays.toString(commandPath));
                                    System.err.println("PUT /api/bookings: " + e.getMessage());
                                    System.err.println("PUT /api/bookings: " + e.getClass());
                                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                                    e.printStackTrace();
                                    return;
                                }
                            }
                        }
                    }
                }

                break;
            case HttpMethod.DELETE:
                break;
        }
    }

    public JSONObject getBookingsJson(ArrayList<Booking> bookings) {
        JSONObject jsonBookingsObject = new JSONObject();
        JSONArray nestedBookingsArray = new JSONArray();
        JSONObject abooking;
        JSONArray aRoomBookingArray;
        for (Booking booking: bookings) {
            abooking = new JSONObject();
            abooking.put("id", booking.getId());
            abooking.put("hotel_id", booking.getHotelId());
            abooking.put("hotel_name", booking.getHotelName());
            abooking.put("customer_id", booking.getCustomerId());
            abooking.put("start_date", booking.getDateRange().getTo().toString());
            abooking.put("end_date", booking.getDateRange().getFrom().toString());
            abooking.put("is_active", booking.getActive());

            aRoomBookingArray = new JSONArray();
            JSONObject aRoomBooking;
            HashMap<Integer, RoomBooking> roomBookingHashMap = booking.getRoomBookings();
            for (Object roomBookingKey: roomBookingHashMap.keySet()){
                RoomBooking roomBooking = roomBookingHashMap.get(roomBookingKey);
                aRoomBooking = new JSONObject();
                aRoomBooking.put("id", roomBooking.getId());
                aRoomBooking.put("is_active", roomBooking.getActive());
                aRoomBooking.put("room_id", roomBooking.getRoomId());
                aRoomBooking.put("main_guest", roomBooking.getMainGuest());
                aRoomBooking.put("no_of_guests", roomBooking.getNumOfGuests());
                aRoomBookingArray.put(aRoomBooking);
            }
            abooking.put("room_bookings", aRoomBookingArray);

            nestedBookingsArray.put(abooking);
        }

        jsonBookingsObject.put("result", nestedBookingsArray);
        return jsonBookingsObject;
    }

    private void handleCreateNewBooking() throws IOException {
//        boolean isValidCustomer = auth.isCustomer() && auth.getId() ==
        boolean allowedToCreateBookings = auth.isCustomer() || auth.isHotelier();
        if (allowedToCreateBookings) {
            executeCreateNewBooking();
        } else {
            responseHelper.unauthorized();
        }
    }

    private void executeCreateNewBooking() throws IOException {
        try {
            useCase = new CreateBooking(dataSource, requestHelper.body().getJSONObject("booking"));
            useCase.execute();
            responseHelper.respond(useCase.getResult(), HttpServletResponse.SC_OK);
        } catch (Exception e) {
            responseHelper.error(e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
