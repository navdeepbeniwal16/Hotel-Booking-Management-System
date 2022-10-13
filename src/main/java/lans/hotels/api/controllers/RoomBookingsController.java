package lans.hotels.api.controllers;

import lans.hotels.use_cases.ViewRoomBookingsForBooking;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;


public class RoomBookingsController extends FrontCommand {
    @Override
    protected void concreteProcess() throws Exception {
        String[] commandPath = request.getPathInfo().split("/");
        switch (request.getMethod()) {
            case HttpMethod.GET:
            case HttpMethod.POST:
                if(commandPath.length == 2) {
                    if(requestHelper.body().has("search")) {
                        JSONObject searchQuery = requestHelper.body().getJSONObject("search");

                        if(searchQuery.has("booking_id")) {
                            int bookingId;
                            try {
                                bookingId = searchQuery.getInt("booking_id");
                            } catch (JSONException e){
                                e.printStackTrace();
                                responseHelper.error("booking_id needs to be integer type", HttpServletResponse.SC_BAD_REQUEST);
                                return;
                            }

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
                            return;
                        }
                    }
                }
            break;
            default:
                responseHelper.unimplemented(request.getMethod() + " /roombookings");
        }
    }
}
