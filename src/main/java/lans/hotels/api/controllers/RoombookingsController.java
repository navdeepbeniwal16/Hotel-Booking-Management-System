package lans.hotels.api.controllers;

import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.datasource.search_criteria.RoomSearchCriteria;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import lans.hotels.domain.room.Room;
import lans.hotels.use_cases.ChangeNumberOfGuests;
import lans.hotels.use_cases.ViewHotelGroupBookings;
import lans.hotels.use_cases.ViewRoomBookingsForBooking;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;


public class RoombookingsController extends FrontCommand {
    @Override
    protected void concreteProcess() throws Exception {
        System.out.println("RoomBookingsController.concreteProcess(): " + request.getMethod() + " " + request.getRequestURI());
        switch (request.getMethod()) {
            case HttpMethod.GET:
                responseHelper.error("GET /bookings: NOT IMPLEMENTED", HttpServletResponse.SC_NOT_IMPLEMENTED);
                return;
            case HttpMethod.POST:
                if(requestHelper.body().has("search")) {
                    JSONObject searchQuery = requestHelper.body().getJSONObject("search");

                    if(searchQuery.has("booking_id")) {
                        if(!searchQuery.has("booking_id")) {
                            responseHelper.error("POST /roombookings search must contain booking_id",HttpServletResponse.SC_BAD_REQUEST);
                            return;
                        }

                        int bookingId = searchQuery.getInt("booking_id");

                        BookingsSearchCriteria bookingsSearchCriteria = new BookingsSearchCriteria();
                        bookingsSearchCriteria.setBookingId(bookingId);

                        Booking booking = null;
                        ArrayList<Booking> bookings = dataSource.findBySearchCriteria(Booking.class, bookingsSearchCriteria);
                        if(bookings.size() > 0)
                            booking = bookings.get(0);

                        if(auth.getId()==booking.getCustomerId()){
                            ViewRoomBookingsForBooking useCase = new ViewRoomBookingsForBooking(dataSource);
                            useCase.setBookingId(bookingId);
                            // @arman
                            useCase.execute(
                                    () -> responseHelper.respondOK(useCase.getResult()),
                                    () -> responseHelper.internalServerError());
                            // OLD CODE example
//                            useCase.execute();
//                            statusCode = useCase.succeeded() ?
//                                    HttpServletResponse.SC_OK :
//                                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
//                            responder.respond(useCase.getResult(), statusCode);
                        }
                        else
                            responseHelper.unauthorized();
                        return;
                    }
                }
                else
                    responseHelper.error("POST /room bookings must contain search",HttpServletResponse.SC_BAD_REQUEST);
                return;
            case HttpMethod.PUT:
                asCustomer(this::handlePut);
                return;
            default:
                responseHelper.unimplemented(request.getMethod() + " /roombookings");
        }
    }

    public Void handlePut() throws Exception {
        if(requestHelper.body().has("room_booking")) {
            JSONObject rbJsonBody = requestHelper.body().getJSONObject("room_booking");

            if(rbJsonBody.has("booking_id")) {

                Integer bookingId = rbJsonBody.getInt("booking_id");
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
                    responseHelper.error("PUT /roombookings booking does not exist", HttpServletResponse.SC_BAD_REQUEST);
                    return null;
                }
                if(!booking.getActive()) {
                    responseHelper.error("PUT /roombookings booking is cancelled", HttpServletResponse.SC_BAD_REQUEST);
                    return null;
                }

                if(booking.getCustomerId()!=auth.getId())
                {
                    responseHelper.unauthorized();
                    return null;
                }

                if(rbJsonBody.has("rb_id")&&rbJsonBody.has("no_of_guests")) {
                    Integer rb_id  = rbJsonBody.getInt("rb_id");
                    Integer no_of_guests  = rbJsonBody.getInt("no_of_guests");

                    HashMap<Integer, RoomBooking> rBookings = booking.getRoomBookings();
                    RoomBooking rBooking = rBookings.get(rb_id);

                    if(rBooking.getNumOfGuests()==no_of_guests){
                        responseHelper.error("PUT /roombookings max guests is the same as value passed", HttpServletResponse.SC_BAD_REQUEST);
                        return null;
                    }

                    ArrayList<Room> rooms = null;
                    Room room = null;
                    RoomSearchCriteria r_criteria = new RoomSearchCriteria();
                    r_criteria.setRoomNumber(rBooking.getRoomId());
                    r_criteria.setHotelId(booking.getHotelId());
                    try {
                        rooms = dataSource.findBySearchCriteria(Room.class, r_criteria);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(rooms.size() > 0)
                        room = rooms.get(0);
                    else {
                        responseHelper.error("PUT /roombookings room does not exist", HttpServletResponse.SC_BAD_REQUEST);
                        return null;
                    }
                    if(room.getMaxOccupancy()<no_of_guests)
                    {
                        responseHelper.error("PUT /roombookings room max occupancy requested is greater than allowed", HttpServletResponse.SC_BAD_REQUEST);
                        return null;
                    }

                    rBooking.setNumOfGuests(no_of_guests);

                    useCase = new ChangeNumberOfGuests(dataSource);
                    useCase.execute();
                    statusCode = useCase.succeeded() ?
                            HttpServletResponse.SC_OK :
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                    responseHelper.respond(useCase.getResult(), statusCode);

                }
            }

        }
        return null;

    }

}
