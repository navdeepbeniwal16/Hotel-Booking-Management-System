package lans.hotels.controllers;

import jdk.jshell.spi.ExecutionControl;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class RoomsController extends FrontCommand {

    @Override
    protected void concreteProcess() throws CommandException, IOException {
        System.out.println("RoomsController.concreteProcess(): " + request.getMethod() + " " + request.getRequestURI());
        switch(request.getMethod()) {
            case HttpMethod.GET:
                try {
                    handleGet();
                } catch (Exception e) {
                    System.err.println("ERROR RoomsContoller:" + e);
                    throw new CommandException(e.getMessage());
                }
                return;
            case HttpMethod.POST:
                System.err.println("POST /rooms: ENDPOINT NOT IMPLEMENTED");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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

    private void handleGet() throws Exception {
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
            ArrayList<Room> rooms;
            try {
                rooms = dataSource.findAll(Room.class);
            } catch (Exception e) {
                System.err.println(e);
                throw new CommandException(e.getMessage());
            }
            if (body.has("search")) {
                RoomSearchCriteria criteria = new RoomSearchCriteria();
                JSONObject searchQueryBody = body.getJSONObject("search");
                if(searchQueryBody.has("hotelId")){
                    try {
                        throw new ExecutionControl.NotImplementedException("room search not implemented");
                    } catch (Exception e){
                        System.err.println("GET /api/rooms SEARCH: " + e);
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                }
            }
            JSONArray roomArray = new JSONArray();
            PrintWriter out = response.getWriter();
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JSONObject aRoom;
            for (Room room: rooms) {
                System.out.println(room.toString());
                aRoom = new JSONObject();
                aRoom.put("id", room.getId());
                aRoom.put("hotelId", room.getHotel().getId());
                aRoom.put("occupancy", room.getSpecification().getOccupancy());
                aRoom.put("address", room.getSpecification().getRoomType());
                roomArray.put(aRoom);
            }

            JSONObject roomJson = new JSONObject();
            roomJson.put("result", roomArray);
            out.print(roomJson);
            out.flush();
        } else {
            System.err.println("GET /rooms: bad request");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
