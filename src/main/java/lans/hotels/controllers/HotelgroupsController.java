package lans.hotels.controllers;

import lans.hotels.api.HttpMethod;
import lans.hotels.datasource.search_criteria.HotelGroupSearchCriteria;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.hotel_group.HotelGroup;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class HotelgroupsController extends FrontCommand {
    @Override
    protected void concreteProcess() throws IOException, SQLException {
        switch(request.getMethod()) {
            case HttpMethod.GET:
                String[] commandPath = request.getPathInfo().split("/");


                if (commandPath.length == 3) {
                    // GET /api/hotels/:id
                    Integer id;
                    try {
                        id = Integer.parseInt(commandPath[2]);
                    } catch (NumberFormatException e) {
                        System.err.println("GET /api/hotel group/:id: " + Arrays.toString(commandPath));
                        System.err.println("GET /api/hotel group/:id: " + e.getMessage());
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                        return;
                    }
                    getHotelGroup(id);
                } else if (commandPath.length==2) {
                    // GET /api/hotelgroups
                    BufferedReader requestReader = request.getReader();

                    String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
                    System.out.println(lines);
                    JSONObject body = new JSONObject();

                    if (lines.length() > 0) {
                        System.out.println(lines);
                        body = new JSONObject(lines);
                    }

                    if (body.has("search")) {
                        HotelGroupSearchCriteria criteria = new HotelGroupSearchCriteria();

                        JSONObject searchQueryBody = body.getJSONObject("search");

                        if(searchQueryBody.has("id")) {
                            Integer hotelGroupId = searchQueryBody.getInt("id");
                            if(hotelGroupId != null) criteria.setHotelGroupID(hotelGroupId);
                        }

                        ArrayList<HotelGroup> hg;
                        try {
                            hg = dataSource.findBySearchCriteria(HotelGroup.class, criteria);
                        } catch (Exception e) {
                            System.err.println("GET /api/hotel group: " + Arrays.toString(commandPath));
                            System.err.println("GET /api/hotels group: " + e.getMessage());
                            System.err.println("GET /api/hotel group: " + e.getClass());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                            return;
                        }

                        JSONArray hgArray = new JSONArray();
                        PrintWriter out = response.getWriter();
                        response.setStatus(200);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JSONObject aHotelGroup;
                        for (HotelGroup hotel_group: hg) {
                            aHotelGroup = new JSONObject();
                            aHotelGroup.put("hotel_id", hotel_group.getID());
                            aHotelGroup.put("name", hotel_group.getName());
                            aHotelGroup.put("address", hotel_group.getAddress().toString());
                            aHotelGroup.put("contact", hotel_group.getPhone());
                            hgArray.put(aHotelGroup);
                        }

                        JSONObject hotelJson = new JSONObject();
                        hotelJson.put("result", hgArray);
                        out.print(hotelJson);
                        out.flush();
                        return;
                    } else {
                        ArrayList<HotelGroup> hotel_groups;
                        try {
                            hotel_groups = dataSource.findAll(HotelGroup.class);
//                            System.out.println("HotelGroupsController.concreteProcess() ALL v1: " + hotel_groups.toString());
//                            System.out.println("HotelGroupsController.concreteProcess() ALL v2: " + dataSource.findAll(HotelGroup.class).toString());
                        } catch (Exception e) {
                            System.err.println("GET /api/hotels: " + Arrays.toString(commandPath));
                            System.err.println("GET /api/hotels: " + e.getMessage());
                            System.err.println("GET /api/hotels: " + e.getClass());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                            return;
                        }

                        JSONArray hotelArray = new JSONArray();
                        PrintWriter out = response.getWriter();
                        response.setStatus(200);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JSONObject aHotel;
                        for (HotelGroup hotelGroup: hotel_groups) {
                            aHotel = new JSONObject();
                            aHotel.put("hotel_id", hotelGroup.getID());
                            aHotel.put("name", hotelGroup.getName());
                            aHotel.put("address", hotelGroup.getAddress().toString());
                            aHotel.put("district", hotelGroup.getPhone());
                            hotelArray.put(aHotel);
                        }

                        JSONObject hotelJson = new JSONObject();
                        hotelJson.put("result", hotelArray);
                        out.print(hotelJson);
                        out.flush();
                        return;
                    }
                } else {
                    System.err.println("Hotels contoller: " + Arrays.toString(commandPath));
                    System.err.println("Hotels contoller: commandPath.length = " + commandPath.length);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                    return;
                }

                return;
            case HttpMethod.POST:
            case HttpMethod.PUT:
            case HttpMethod.DELETE:
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        }
    }

    private void getHotelGroup(Integer id) throws IOException, SQLException {

        HotelGroup hg = dataSource.find(HotelGroup.class, id);

        if (hg == null) {
            System.out.println("404 NOT FOUND");
            System.out.println("\tHotelController.getHotelGroup(): hotel group.id=" + id);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        } else {
            JSONObject json = new JSONObject();
            PrintWriter out = response.getWriter();
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            json.put("id", hg.getId());
            json.put("name", hg.getName());
            json.put("address", hg.getAddress());
            json.put("phone",hg.getPhone());
            out.print(json);
            out.flush();
        }
    }
}
