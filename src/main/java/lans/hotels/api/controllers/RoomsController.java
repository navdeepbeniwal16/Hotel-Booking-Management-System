package lans.hotels.api.controllers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.room.Room;
import lans.hotels.use_cases.AddRoomToHotel;
import lans.hotels.use_cases.Utils;
import lans.hotels.use_cases.ViewHotelRooms;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.text.ParseException;
import java.util.Date;

public class RoomsController extends FrontCommand {

    @Override
    protected void concreteProcess() throws IOException {
        System.out.println("RoomsController.concreteProcess(): " + request.getMethod() + " " + request.getRequestURI());
        switch(request.getMethod()) {
            case HttpMethod.GET: // TODO: Use ViewHotelRoomsHotelier use case
                sendJsonErrorResponse("GET /rooms: NOT IMPLEMENTED", HttpServletResponse.SC_NOT_IMPLEMENTED);
                return;
            case HttpMethod.POST:
                try {
                    handlePost();
                } catch (Exception e) {
                    sendJsonErrorResponse(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                return;
            case HttpMethod.PUT:
                sendJsonErrorResponse("PUT /rooms: NOT IMPLEMENTED", HttpServletResponse.SC_NOT_IMPLEMENTED);
                return;
            case HttpMethod.DELETE:
                sendJsonErrorResponse("DELETE /rooms: NOT IMPLEMENTED", HttpServletResponse.SC_NOT_IMPLEMENTED);
                return;
            default:
                sendJsonErrorResponse("invalid request", HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void handlePost() throws Exception {
        if (requestBody.has("search")) {
            JSONObject searchQueryBody = requestBody.getJSONObject("search");
            handleSearchQuery(searchQueryBody);
        } else if (requestBody.has("room")) {
            handleRoomQuery(requestBody);
        } else {
            sendJsonErrorResponse("POST /rooms does must include 'room' or 'search'", HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private Room getRoomFromJsonObject(JSONObject jsonObject) throws InvalidObjectException {

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
        if (r == null)
            throw new InvalidObjectException("Failed to parse room object from request body");
        return r;
    }

    private void handleRoomQuery(JSONObject body) throws IOException {
        if (!auth.isHotelier()) {
            sendUnauthorizedJsonResponse();
            return;
        }
        Room r = getRoomFromJsonObject(body);
        useCase = new AddRoomToHotel(dataSource); // TODO
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        sendJsonResponse(response, useCase.getResult(), statusCode);
    }


    private void handleSearchQuery(JSONObject searchParams) throws Exception {
        Integer hotelId = parseHotelId(searchParams);
        Utils.RoomResultsInclude include = parseInclude(searchParams);
        Date startDate = parseDate(searchParams, "start_date");
        Date endDate =  parseDate(searchParams, "end_date");
        ViewHotelRooms viewHotelRooms = new ViewHotelRooms(dataSource, hotelId);
        viewHotelRooms.setInclude(include);
        viewHotelRooms.setStartDate(startDate);
        viewHotelRooms.setEndDate(endDate);
        viewHotelRooms.execute();
        statusCode = viewHotelRooms.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        sendJsonResponse(response, viewHotelRooms.getResult(), statusCode);
    }

    private Integer parseHotelId(JSONObject searchQueryBody) throws JSONException {
        return searchQueryBody.getInt("hotel_id");
    }

    private Utils.RoomResultsInclude parseInclude(JSONObject searchQueryBody) {
        if (!searchQueryBody.has("is_available")) return Utils.RoomResultsInclude.ALL;
        return searchQueryBody.getBoolean("is_available") ?
                Utils.RoomResultsInclude.AVAILABLE :
                Utils.RoomResultsInclude.UNAVAILABLE;
    }

    private Date parseDate(JSONObject searchQueryBody, String selectDate) throws Exception {
        String dateString = null;
        if (selectDate.equals("start_date")) {
            dateString = "01/01/1900";
        } else if (selectDate.equals("end_date")) {
            dateString = "31/12/2100";
        } else {
            throw new Exception("invalid date selection: " + selectDate);
        }

        if (searchQueryBody.has(selectDate)) {
            dateString = searchQueryBody.getString(selectDate);
        }

        return dateFormat.parse(dateString);
    }
}
