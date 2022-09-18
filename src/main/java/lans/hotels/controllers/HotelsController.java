package lans.hotels.controllers;

import lans.hotels.api.HttpMethod;
import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.datasource.mappers.HotelDataMapper;
import lans.hotels.domain.hotel.Hotel;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HotelsController extends FrontCommand {
    @Override
    protected void concreteProcess() throws IOException {
        switch(request.getMethod()) {
            case HttpMethod.GET:
                String[] commandPath = request.getPathInfo().split("/");


                if (commandPath.length == 3) {
                    // GET /api/hotels/:id
                    Integer id;
                    try {
                        id = Integer.parseInt(commandPath[2]);
                    } catch (NumberFormatException e) {
                        System.err.println("getHotel(): " + Arrays.toString(commandPath));
                        System.err.println("getHotel(): " + e.getMessage());
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                        return;
                    }
                    getHotel(id);
                } else if (commandPath.length==2) {
                    // GET /api/hotels
                    BufferedReader requestReader = request.getReader();

                    String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
                    JSONObject body = (JSONObject) JSONObject.stringToValue(lines);

                    if (body.has("search")) {
                        // perform search
                    } else {
                        ArrayList<Hotel> hotels;
                        try {
                            hotels = (ArrayList<Hotel>) dataSource.findAll(Hotel.class);
                        } catch (Exception e) {
                            System.err.println("getHotel(): " + Arrays.toString(commandPath));
                            System.err.println("getHotel(): " + e.getMessage());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                            return;
                        }

                        JSONArray hotelArray = new JSONArray();
                        PrintWriter out = response.getWriter();
                        response.setStatus(200);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JSONObject aHotel;
                        for (Hotel hotel: hotels) {
                            aHotel = new JSONObject();
                            aHotel.put("id", hotel.getId());
                            aHotel.put("name", hotel.getName());
                            aHotel.put("email", hotel.getEmail());
                            aHotel.put("address", hotel.getAddress());
                            aHotel.put("phone", hotel.getPhone().toString());
                            hotelArray.put(aHotel);
                        }

                        JSONObject hotelJson = new JSONObject();
                        hotelJson.put("result", hotelArray);
                        out.print(hotelJson);
                        out.flush();
                        return;
                    }
                } else {
                    System.err.println("getHotel(): " + Arrays.toString(commandPath));
                    System.err.println("getHotel(): commandPath.length = " + commandPath.length);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                    return;
                 }

                return;
            case HttpMethod.PUT:
            case HttpMethod.POST:
            case HttpMethod.DELETE:
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        }
    }

    private void getHotel(Integer id) throws IOException {

        Hotel hotel = (Hotel) dataSource.find(Hotel.class, id);

        if (hotel == null) {
            System.out.println("404 NOT FOUND");
            System.out.println("\tHotelController.getHotel(): hotel.id=" + id);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        } else {
            JSONObject json = new JSONObject();
            PrintWriter out = response.getWriter();
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            json.put("id", hotel.getId());
            json.put("name", hotel.getName());
            json.put("email", hotel.getEmail());
            json.put("address", hotel.getAddress());
            json.put("phone", hotel.getPhone().toString());
            out.print(json);
            out.flush();
        }
    }
}
