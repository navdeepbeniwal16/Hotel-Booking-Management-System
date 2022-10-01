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
    protected void concreteProcess() throws CommandException, IOException, SQLException {
        String[] commandPath = request.getPathInfo().split("/");
        int statusCode;
        switch (request.getMethod()) {
            case HttpMethod.GET:
            case HttpMethod.POST:
                if(commandPath.length == 2) {
                    JSONObject requestBody = getRequestBody(request);

                    if(requestBody.has("search")) {
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

                            ViewRoomBookingsForBooking useCase = new ViewRoomBookingsForBooking(dataSource);
                            useCase.setBookingId(bookingId);
                            useCase.execute();
                            statusCode = useCase.succeeded() ?
                                    HttpServletResponse.SC_OK :
                                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                            sendJsonResponse(response, useCase.getResult(), statusCode);
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
