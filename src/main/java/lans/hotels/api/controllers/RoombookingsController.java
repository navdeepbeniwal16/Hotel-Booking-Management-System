package lans.hotels.api.controllers;

import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
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
                else
                    throw new Exception("No booking with id found");

                if(booking.getCustomerId()!=auth.getId())
                {
                    responseHelper.unauthorized();
                    return null;
                }

                if(rbJsonBody.has("rb_id")&&rbJsonBody.has("no_of_guests")) {
                    Integer rb_id  = rbJsonBody.getInt("rb_id");
                    Integer no_of_guests  = rbJsonBody.getInt("no_of_guests");

                    useCase = new ChangeNumberOfGuests(dataSource, booking, rb_id, no_of_guests);
                    useCase.execute();
                    statusCode = useCase.succeeded() ?
                            HttpServletResponse.SC_OK :
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                    responseHelper.respond(useCase.getResult(), statusCode);

                }
            }

        }
        return null;

//        JSONObject rbObject = roomBookingsArray.getJSONObject(rbIndex);
//        HashMap<Integer, RoomBooking> rBookings = booking.getRoomBookings();
//        for(Object rbKey: rBookings.keySet()) {
//            RoomBooking rBooking = rBookings.get(rbKey);
//
//            System.out.println("Reaching here..."); // TODO: TBR
//            if(rbObject.has("id") && rbObject.getInt("id") == rBooking.getId()) {
//                if(rbObject.has("no_of_guests") && rbObject.getInt("no_of_guests") != rBooking.getNumOfGuests()) {
//                    rBooking.setNumOfGuests(rbObject.getInt("no_of_guests"));
//                }
//            }
//        }
    }

}
