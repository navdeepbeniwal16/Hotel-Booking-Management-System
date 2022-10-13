package lans.hotels.api.controllers;

import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.domain.booking.Booking;
import lans.hotels.use_cases.ViewRoomBookingsForBooking;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;


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
                return;
            default:
                responseHelper.unimplemented(request.getMethod() + " /roombookings");
        }
    }
}
