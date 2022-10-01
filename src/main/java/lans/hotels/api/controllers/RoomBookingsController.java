package lans.hotels.api.controllers;

import lans.hotels.api.exceptions.CommandException;
import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class RoomBookingsController extends FrontCommand {
    @Override
    protected void concreteProcess() throws CommandException, IOException, SQLException {
        String[] commandPath = request.getPathInfo().split("/");
        int statusCode;
        switch (request.getMethod()) {
            case HttpMethod.GET:
            case HttpMethod.POST:
            {
                JSONObject requestBody = getRequestBody(request);

                if(requestBody.has("search"))
                {
                    JSONObject searchQuery = requestBody.getJSONObject("search");

                    if(searchQuery.has("booking_id")) {
                        int bookingId;
                        try {
                            bookingId = searchQuery.getInt("booking_id");
                        } catch (JSONException e){
                            e.printStackTrace();
                            sendJsonErrorResponse(response, "booking_id needs to be integer type", HttpServletResponse.SC_BAD_REQUEST);
                            return;
                        }

                        Booking booking = null;
                        try {
                            BookingsSearchCriteria bookingsSearchCriteria = new BookingsSearchCriteria();
                            bookingsSearchCriteria.setBookingId(bookingId);
                            ArrayList<Booking> bookings = dataSource.findBySearchCriteria(Booking.class, bookingsSearchCriteria);
                            if(bookings.size() > 0) booking = bookings.get(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                            sendJsonErrorResponse(response, "unknown server error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            return;
                        }

                        HashMap<Integer, RoomBooking> roomBookings = new HashMap<>();
                        if(booking != null) { roomBookings = booking.getRoomBookings(); }

                        JSONObject roomBookingsJson = prepareRoomBookingsJson(roomBookings);
                        sendResponse(roomBookingsJson);
                    }
                }
            }

            break;
            case HttpMethod.PUT:
                break;
            case HttpMethod.DELETE:
                break;
        }

        return;
    }

    public JSONObject prepareRoomBookingsJson(HashMap<Integer, RoomBooking> roomBookingHashMap) {
        JSONObject response = new JSONObject();
        JSONArray results = new JSONArray();
        for(RoomBooking roomBooking: roomBookingHashMap.values()) {
            JSONObject roomBookingJson = new JSONObject();
            roomBookingJson.put("id", roomBooking.getId());
            roomBookingJson.put("room_id", roomBooking.getRoomId());
            roomBookingJson.put("main_guest", roomBooking.getMainGuest());
            roomBookingJson.put("number_of_guests", roomBooking.getNumOfGuests());
            results.put(roomBookingJson);
        }
        response.put("results", results);
        return response;
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
