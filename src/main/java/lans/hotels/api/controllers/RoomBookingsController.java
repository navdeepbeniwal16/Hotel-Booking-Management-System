package lans.hotels.api.controllers;

import lans.hotels.api.exceptions.CommandException;
import lans.hotels.use_cases.ViewRoomBookingsForBooking;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


public class RoomBookingsController extends FrontCommand {
    @Override
    protected void concreteProcess() throws Exception {
        String[] commandPath = request.getPathInfo().split("/");
        switch (request.getMethod()) {
            case HttpMethod.GET:
            case HttpMethod.POST:
                if(commandPath.length == 2) {
                    if(requestBody.has("search")) {
                        JSONObject searchQuery = requestBody.getJSONObject("search");

                        if(searchQuery.has("booking_id")) {
                            int bookingId;
                            try {
                                bookingId = searchQuery.getInt("booking_id");
                            } catch (JSONException e){
                                e.printStackTrace();
                                responder.error("booking_id needs to be integer type", HttpServletResponse.SC_BAD_REQUEST);
                                return;
                            }

                            ViewRoomBookingsForBooking useCase = new ViewRoomBookingsForBooking(dataSource);
                            useCase.setBookingId(bookingId);
                            // @arman
                            useCase.execute(
                                    () -> responder.respondOK(useCase.getResult()),
                                    () -> responder.internalServerError());
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
            case HttpMethod.PUT:
                break;
            case HttpMethod.DELETE:
                break;
        }

        return;
    }
}
