package lans.hotels.api.controllers;

import lans.hotels.api.exceptions.CommandException;
import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.datasource.search_criteria.CustomerSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelGroupSearchCriteria;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;

import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.user_types.Customer;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class BookingsController extends FrontCommand {
    @Override
    protected void concreteProcess() throws CommandException, IOException, SQLException {
        String[] commandPath = request.getPathInfo().split("/");
        switch (request.getMethod()) {
            case HttpMethod.GET:
                if(commandPath.length == 2) {

                    // GET: /api/bookings
                    JSONObject requestBody = getRequestBody(request);
                    if(requestBody.has("search")) {
                        JSONObject searchQuery = requestBody.getJSONObject("search");
                        if(searchQuery.has("customer_id")) { // single customer query
                            CustomerSearchCriteria customerSearchCriteria = new CustomerSearchCriteria();

                            Integer customerId;
                            try {
                                customerId = searchQuery.getInt("customer_id");
                            } catch (Exception e) {
                                System.err.println("GET /api/bookings: " + Arrays.toString(commandPath));
                                System.err.println("GET /api/bookings: " + e.getMessage());
                                System.err.println("GET /api/bookings: " + e.getClass());
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                                return;
                            }

                            if(customerId != null) customerSearchCriteria.setCustomerId(customerId);
                            Customer customer;
                            try {
                                // Fetching the customer based on the customer_id
                                ArrayList<Customer> customers = dataSource.findBySearchCriteria(Customer.class, customerSearchCriteria);
                                if(!customers.isEmpty()) {
                                    customer = customers.get(0);

                                    // Making call to customer to fetch all of it's booking details
                                    ArrayList<Booking> customerBookings = customer.getAllBookings();
                                    JSONObject customersBookingsJson = getBookingsJson(customerBookings);
                                    sendResponse(customersBookingsJson);
                                } else {
                                    System.err.println("GET /api/bookings: " + Arrays.toString(commandPath));
                                    response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
                                }

                            } catch (Exception e) {
                                System.err.println("GET /api/bookings: " + Arrays.toString(commandPath));
                                System.err.println("GET /api/bookings: " + e.getMessage());
                                System.err.println("GET /api/bookings: " + e.getClass());
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                                return;
                            }
                        }

                        else if(searchQuery.has("hotel_group_id")) { // TODO: Needs to be tackled, not using the right business rule
                            Integer hotelGroupID = searchQuery.getInt("hotel_group_id");
                            HotelGroupSearchCriteria hgSearchCriteria = new HotelGroupSearchCriteria();
                            if(hotelGroupID != null) hgSearchCriteria.setHotelGroupID(hotelGroupID);

                            HotelGroup hg;
                            try {
                                // Fetching the hotel-group based on the customer id
                                ArrayList<HotelGroup> hotelgroups = dataSource.findBySearchCriteria(HotelGroup.class, hgSearchCriteria);
                                if(!hotelgroups.isEmpty()) {
                                    System.out.println("Hi");
                                    hg = hotelgroups.get(0);

                                    // Making call to hotel group to fetch all of it's booking details
                                    ArrayList<Booking> hg_bookings = hg.getAllBookings();


                                    for(Booking booking: hg_bookings) {
                                        System.out.println("Booking : " + booking.getId());
                                    }
                                    JSONObject hgBookingsJSON = getBookingsJson(hg_bookings);
                                    sendResponse(hgBookingsJSON);
                                } else {
                                    System.out.println("Not a hotel group bookings query!!!");
                                }

                            } catch (Exception e) {
                                System.err.println("GET /api/bookings: " + Arrays.toString(commandPath));
                                System.err.println("GET /api/bookings: " + e.getMessage());
                                System.err.println("GET /api/bookings: " + e.getClass());
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                                return;
                            }
                        }
                    }

                }
                break;
            case HttpMethod.POST:

                break;
            case HttpMethod.PUT:

                // PUT: api/bookings
                if(commandPath.length == 2) {
                    JSONObject requestBody = getRequestBody(request);

                    if(requestBody.has("booking")) {
                        JSONObject bookingJsonBody = requestBody.getJSONObject("booking");

                        if(!bookingJsonBody.has("id")) {
                            System.err.println("GET /api/bookings: " + Arrays.toString(commandPath));
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                            return;
                        } else {
                            Integer bookingId = bookingJsonBody.getInt("id");

                            if(bookingId != null) {
                                // fetching booking object from the backend
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

                                        if(bookingJsonBody.has("room_bookings") & bookingJsonBody.getJSONArray("room_bookings").length() > 0) {
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

                                        dataSource.commit();

                                    } else {
                                        System.err.println("PUT /api/bookings: " + Arrays.toString(commandPath));
                                        response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
                                        return;
                                    }

                                } catch (Exception e) {
                                    System.err.println("GET /api/bookings: " + Arrays.toString(commandPath));
                                    System.err.println("GET /api/bookings: " + e.getMessage());
                                    System.err.println("GET /api/bookings: " + e.getClass());
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

    public JSONObject getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader requestReader = request.getReader();

        String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println("Request Body Lines + " + lines);
        JSONObject body;
        if (lines.length() > 0) {
            System.out.println(lines);
            body = new JSONObject(lines);
        } else {
            return null;
        }
        return body;
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

    public void sendResponse(JSONObject jsonObject) {
        try {
            PrintWriter out = response.getWriter();
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(jsonObject);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
