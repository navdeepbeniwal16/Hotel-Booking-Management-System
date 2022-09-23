package lans.hotels.controllers;

import lans.hotels.api.HttpMethod;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.utils.DateRange;
import java.sql.Date;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SearchhotelController extends FrontCommand {
    @Override
    protected void concreteProcess() throws IOException, SQLException {
        String[] commandPath = request.getPathInfo().split("/");

        if (commandPath.length != 2) {
            System.err.println("Search controller: " + Arrays.toString(commandPath));
            System.err.println("Search controller: commandPath.length = " + commandPath.length);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
            return;
        } else {
            ArrayList<Hotel> hotels;
            switch (request.getMethod()) {
                case HttpMethod.GET:

                case HttpMethod.POST: {

                    JSONObject body = getRequestBody(request);

                    if (body.has("search")) {
                        HotelSearchCriteria criteria = new HotelSearchCriteria();

                        JSONObject searchQueryBody = body.getJSONObject("search");

                        if (searchQueryBody.has("city") &&
                                searchQueryBody.has("start_date") && searchQueryBody.has("end_date")) {
                            String city = searchQueryBody.getString("city");

                            Date start_date = Date.valueOf(searchQueryBody.getString("start_date"));
                            Date end_date = Date.valueOf(searchQueryBody.getString("end_date"));

                            DateRange date_range = new DateRange(start_date,end_date);

                            System.out.println("From : "+date_range.getFrom().toString()+", To : "+date_range.getFrom().toString());
                            if (city != null) criteria.setCity(city);
                            System.out.println(criteria.getCity());
                            if (date_range != null) criteria.setDateRange(date_range);
                            System.out.println(criteria.getDateRange().getFrom().toString());

                        }

                        try {
                            hotels = dataSource.findBySearchCriteria(Hotel.class, criteria);
                        } catch (Exception e) {
                            System.err.println("GET /api/hotels: " + Arrays.toString(commandPath));
                            System.err.println("GET /api/hotels: " + e.getMessage());
                            System.err.println("GET /api/hotels: " + e.getClass());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                            return;
                        }


                        returnHotelJSON(hotels);
                        return;

                    } else {
                        System.err.println("POST /api/search: " + Arrays.toString(commandPath));
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                        return;
                    }
                }
                case HttpMethod.PUT:
                case HttpMethod.DELETE:
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
            }

        }

    }

    public void returnHotelJSON(ArrayList<Hotel> hotels) throws IOException {
        JSONArray hotelArray = new JSONArray();
        PrintWriter out = response.getWriter();
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject aHotel;
        for (Hotel hotel: hotels) {
            aHotel = new JSONObject();
            aHotel.put("hotel_group_id", hotel.getHotelGroupID());
            aHotel.put("hotel_id", hotel.getID());
            aHotel.put("name", hotel.getName());
            aHotel.put("email", hotel.getEmail());
            aHotel.put("address", hotel.getAddress().toString());
            aHotel.put("contact", hotel.getContact());
            aHotel.put("city", hotel.getCity());
            aHotel.put("pin_code", hotel.getPinCode());
            aHotel.put("district", hotel.getAddress().getDistrict().toString());
            aHotel.put("is_active", hotel.getIsActive());
            hotelArray.put(aHotel);
        }

        JSONObject hotelJson = new JSONObject();
        hotelJson.put("result", hotelArray);
        out.print(hotelJson);
        out.flush();
        return;

    }

    public JSONObject getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader requestReader = request.getReader();

        String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println("Request Body Lines + " + lines);
        JSONObject body;
        if (lines.length() > 0) {
            System.out.println(lines);
            body = new JSONObject(lines);
        } else {
            return null;
        }
        return body;
    }


}
