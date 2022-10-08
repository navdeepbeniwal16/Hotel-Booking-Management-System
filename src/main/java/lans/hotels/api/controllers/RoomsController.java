package lans.hotels.api.controllers;

import lans.hotels.api.exceptions.CommandException;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.room.Room;
import lans.hotels.use_cases.AddRoomToHotel;
import lans.hotels.use_cases.Utils;
import lans.hotels.use_cases.ViewHotelRoomsHotelier;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class RoomsController extends FrontCommand {

    @Override
    protected void concreteProcess() throws CommandException, IOException {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String[] commandPath = request.getPathInfo().split("/");
        int statusCode;

        System.out.println("RoomsController.concreteProcess(): " + request.getMethod() + " " + request.getRequestURI());
        switch(request.getMethod()) {
            case HttpMethod.GET: // TODO: Use ViewHotelRoomsHotelier use case
                System.err.println("GET /rooms: NOT IMPLEMENTED");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;

            case HttpMethod.POST:{
                JSONObject body = getRequestBody(request);
                if (body.has("search")) {
                    JSONObject searchQueryBody = body.getJSONObject("search");

                    if (searchQueryBody.has("hotel_id")) {
                        Integer hotelId = null;
                        Utils.RoomResultsInclude include = Utils.RoomResultsInclude.ALL;
                        Date startDate = null;
                        Date endDate = null;

                        try {
                            hotelId = searchQueryBody.getInt("hotel_id");

                            if(searchQueryBody.has("is_available")) {
                                if(searchQueryBody.has("start_date") && searchQueryBody.has("end_date")) {
                                    if(searchQueryBody.getBoolean("is_available")) include = Utils.RoomResultsInclude.AVAILABLE;
                                    if(!searchQueryBody.getBoolean("is_available")) include = Utils.RoomResultsInclude.UNAVAILABLE;

                                    String startDateString = searchQueryBody.getString("start_date");
                                    String endDateString = searchQueryBody.getString("end_date");
                                    startDate = format.parse(startDateString);
                                    endDate = format.parse(endDateString);
                                } else {
                                    throw new RuntimeException("start_date & end_date needs to be provided");
                                }
                            }
                        } catch (Exception e) {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                            //  TODO: Send a more detailed response
                        }

                        // Calling different use cases for different users and search criteria
                        if(auth.isAdmin() || auth.isHotelier() || (auth.isCustomer() && include == Utils.RoomResultsInclude.AVAILABLE)) {
                            ViewHotelRoomsHotelier viewHotelRoomsHotelierUC = new ViewHotelRoomsHotelier(dataSource, hotelId);
                            viewHotelRoomsHotelierUC.setInclude(include);
                            if(startDate != null && endDate != null) {
                                viewHotelRoomsHotelierUC.setStartDate(startDate);
                                viewHotelRoomsHotelierUC.setEndDate(endDate);
                            }
                            viewHotelRoomsHotelierUC.execute();
                            statusCode = viewHotelRoomsHotelierUC.succeeded() ?
                                    HttpServletResponse.SC_OK :
                                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                            sendJsonResponse(response, viewHotelRoomsHotelierUC.getResult(), statusCode);
                        } else {
                            sendUnauthorizedJsonResponse();
                        }
                        return;

                    }
                    return;
                }
                else if (body.has("room")) {
                    if (!auth.isHotelier()) {
                        sendUnauthorizedJsonResponse();
                        return;
                    }
                    Room r = getRoomFromJsonObject(body);

                    if (r == null)
                        throw new InvalidObjectException("Failed to parse room object from request body");
                    useCase = new AddRoomToHotel(dataSource);
                    useCase.execute();
                    statusCode = useCase.succeeded() ?
                            HttpServletResponse.SC_OK :
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                    sendJsonResponse(response, useCase.getResult(), statusCode);
                    return;

                }
                else {
                    System.err.println("POST /api/hotels: " + Arrays.toString(commandPath));
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                    return;
                }
            }
            case HttpMethod.PUT:
                System.err.println("PUT /rooms: NOT IMPLEMENTED");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            case HttpMethod.DELETE:
                System.err.println("DELETE /rooms: bad request");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public Room getRoomFromJsonObject(JSONObject jsonObject) {

        Room r = null;
        Integer hotel_id = null;
        String type = null;
        Integer max_occupancy = null;
        String bed_type = null;
        Integer room_price = null;
        Integer number = null;
        Boolean is_active = true;


        if(jsonObject.has("room")) {
            JSONObject nestedJsonObject = jsonObject.getJSONObject("room");

            if(nestedJsonObject.has("hotel_id"))
                hotel_id = nestedJsonObject.getInt("hotel_id");
            if(nestedJsonObject.has("type"))
                type = nestedJsonObject.getString("type");
            if(nestedJsonObject.has("max_occupancy"))
                max_occupancy = nestedJsonObject.getInt("max_occupancy");
            if(nestedJsonObject.has("bed_type"))
                bed_type = nestedJsonObject.getString("bed_type");
            if(nestedJsonObject.has("room_price"))
                room_price = nestedJsonObject.getInt("room_price");
            if(nestedJsonObject.has("number"))
                number = nestedJsonObject.getInt("number");

            try {
                r = new Room(dataSource,
                        hotel_id,
                        type,
                        max_occupancy,
                        bed_type,
                        room_price,
                        number,
                        is_active);
            } catch (UoWException e) {
                e.printStackTrace();
            }
        }
        return r;
    }
}
