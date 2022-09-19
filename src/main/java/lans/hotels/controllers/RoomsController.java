package lans.hotels.controllers;

import lans.hotels.api.HttpMethod;
import lans.hotels.api.exceptions.CommandException;
import lans.hotels.datasource.search_criteria.RoomSearchCriteria;
import lans.hotels.domain.room.Room;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RoomsController extends FrontCommand {

    @Override
    protected void concreteProcess() throws CommandException, IOException {
        switch(request.getMethod()) {
            case HttpMethod.GET:
                handleGet();
                return;
            case HttpMethod.POST:
                System.err.println("POST /rooms: ENDPOINT NOT IMPLEMENTED");
                return;
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

    private void handleGet() throws IOException {
        String[] commandPath = request.getPathInfo().split("/");
        BufferedReader requestReader = request.getReader();
        String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject body = new JSONObject();

        if (lines.length() > 0) {
            System.out.println(lines);
            body = new JSONObject(lines);
        }

        if (commandPath.length == 3) {
            // GET /api/rooms/:id
            Integer id;
            try {
                id = Integer.parseInt(commandPath[2]);
                System.err.println("GET /rooms/:id: NOT IMPLEMENTED");
                System.err.println(commandPath.toString());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                System.err.println("GET /api/rooms/:id: " + Arrays.toString(commandPath));
                System.err.println("GET /api/rooms/:id: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
            }
        } else if (commandPath.length==2) {
            if (body.has("search")) {
                RoomSearchCriteria criteria = new RoomSearchCriteria();
                JSONObject searchQueryBody = body.getJSONObject("search");
                if(searchQueryBody.has("hotelId")){
                    try {
                        System.out.println(searchQueryBody.get("hotelId"));
                        System.out.println(dataSource);
                        List<Room> rooms = (List<Room>) dataSource.findAll(Room.class);
                        System.out.println(rooms.get(0).toString());
                        JSONArray roomArray = new JSONArray();
                        PrintWriter out = response.getWriter();
                        response.setStatus(200);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        JSONObject aRoom;
                        for (Room room: rooms) {
//                            if (room.getId() == searchQueryBody.get("hotelId")) {
                            System.out.println(room.toString());
                                aRoom = new JSONObject();
                                aRoom.put("id", room.getId());
                                aRoom.put("hotelId", room.getHotel().getId());
                                aRoom.put("occupancy", room.getSpecification().getCapacity());
                                aRoom.put("address", room.getSpecification().getRoomType());
                                roomArray.put(aRoom);
//                            }
                        }

                        JSONObject roomJson = new JSONObject();
                        roomJson.put("result", roomArray);
                        out.print(roomJson);
                        out.flush();
                    } catch (Exception e){
                        System.err.println("GET /api/rooms SEARCH: " + e);
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                }
            }
        } else {
            System.err.println("GET /rooms: bad request");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
