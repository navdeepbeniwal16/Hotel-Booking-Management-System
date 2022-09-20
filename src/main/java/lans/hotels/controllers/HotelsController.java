package lans.hotels.controllers;

import lans.hotels.api.HttpMethod;
import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.datasource.mappers.HotelDataMapper;
import lans.hotels.datasource.search_criteria.HotelsSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.District;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HotelsController extends FrontCommand {
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
                        System.err.println("GET /api/hotels/:id: " + Arrays.toString(commandPath));
                        System.err.println("GET /api/hotels/:id: " + e.getMessage());
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                        return;
                    }
                    getHotel(id);
                } else if (commandPath.length==2) {
                    // GET /api/hotels
                    BufferedReader requestReader = request.getReader();

                    String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
                    System.out.println(lines);
                    JSONObject body = new JSONObject();

                    if (lines.length() > 0) {
                        System.out.println(lines);
                         body = new JSONObject(lines);
                    }

                    if (body.has("search")) {
                        HotelsSearchCriteria criteria = new HotelsSearchCriteria();

                        JSONObject searchQueryBody = body.getJSONObject("search");
                        if(searchQueryBody.has("location")){
                            String location = searchQueryBody.getString("location");
                            if(location != null) criteria.setLocation(location);
                        }

                        if(searchQueryBody.has("startDate") && searchQueryBody.has("endDate")) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

                            String startDateInString = searchQueryBody.getString("startDate");
                            String endDateInString = searchQueryBody.getString("endDate");
                            try {
                                Date startDate = formatter.parse(startDateInString);
                                Date endDate = formatter.parse(endDateInString);
                                criteria.setStartDate(startDate);
                                criteria.setEndDate(endDate);
                            } catch (Exception e) {
                                System.err.println("GET /api/hotels: " + Arrays.toString(commandPath));
                                System.err.println("GET /api/hotels: " + e.getMessage());
                                System.err.println("GET /api/hotels: " + e.getClass());
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                                return;
                            }
                        }

                        if(searchQueryBody.has("hotelGroupId")) {
                           Integer hotelGroupId = searchQueryBody.getInt("hotelGroupId");
                           if(hotelGroupId != null) criteria.setHotelGroupId(hotelGroupId);
                        }

                        if(searchQueryBody.has("id")) {
                            Integer hotelId = searchQueryBody.getInt("id");
                            if(hotelId != null) criteria.setHotelId(hotelId);
                        }

                        ArrayList<Hotel> hotels;
                        try {
                            hotels = dataSource.findBySearchCriteria(Hotel.class, criteria);
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
                    } else {
                        ArrayList<Hotel> hotels;
                        try {
                            hotels = dataSource.findAll(Hotel.class);
                            System.out.println("HotelsController.concreteProcess() ALL v1: " + hotels.toString());
                            System.out.println("HotelsController.concreteProcess() ALL v2: " + dataSource.findAll(Hotel.class).toString());
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
                } else {
                    System.err.println("Hotels contoller: " + Arrays.toString(commandPath));
                    System.err.println("Hotels contoller: commandPath.length = " + commandPath.length);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                    return;
                 }

                return;
            case HttpMethod.POST:
            {
                String[] commandPath2 = request.getPathInfo().split("/");

                if (commandPath2.length == 2) {

                    JSONObject jsonHotelObject = getRequestBody(request);
                    System.out.println("Parsed Hotel JSONObject : " + jsonHotelObject);
                    Hotel h = getHotelGroupFromJsonObject(jsonHotelObject);

                    if(h == null)
                        throw new InvalidObjectException("Failed to parse hotel group object from request body");

                    System.out.println("Parsed Hotel Object : " + h);

                    boolean success;
                    try{
                        success = dataSource.insert(Hotel.class,h);
                    } catch (Exception e) {
                        System.err.println("POST /api/hotels: " + Arrays.toString(commandPath2));
                        System.err.println("POST /api/hotels: " + e.getMessage());
                        System.err.println("POST /api/hotels: " + e.getClass());
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                        return;
                    }

                    PrintWriter out = response.getWriter();
                    response.setStatus(200);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    JSONObject aHG;
                    aHG = new JSONObject();
                    if(success)
                        aHG.put("created", success);
                    else
                        aHG.put("created",success);

                    JSONObject hgJSON = new JSONObject();
                    hgJSON.put("result", aHG);
                    out.print(hgJSON);
                    out.flush();
                    return;


                }
                else {
                    System.err.println("Hotel Group controller: " + Arrays.toString(commandPath2));
                    System.err.println("Hotel Group controller: commandPath2.length = " + commandPath2.length);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                    return;
                }
            }
            case HttpMethod.PUT:
            case HttpMethod.DELETE:
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        }
    }

    private void getHotel(Integer id) throws IOException, SQLException {

        Hotel hotel = dataSource.find(Hotel.class, id);

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
            out.print(json);
            out.flush();
        }
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

    public Hotel getHotelGroupFromJsonObject(JSONObject jsonObject) {

        Hotel h = null;
        int hotel_group_id = 0;
        String h_name = "";
        String email ="";
        String l1 = "";
        String l2 = "";
        String city = "";
        int postcode = 0;
        String district = "";
        String contact = "";
        boolean is_active = true;

        if(jsonObject.has("hotel")) {
            JSONObject nestedJsonObject = jsonObject.getJSONObject("hotel");

            if(nestedJsonObject.has("hotel_group_id"))
                hotel_group_id = nestedJsonObject.getInt("hotel_group_id");
            if(nestedJsonObject.has("h_name"))
                h_name = nestedJsonObject.getString("h_name");
            if(nestedJsonObject.has("email"))
                email = nestedJsonObject.getString("email");
            if(nestedJsonObject.has("l1"))
                l1 = nestedJsonObject.getString("l1");
            if(nestedJsonObject.has("l2"))
                l2 = nestedJsonObject.getString("l2");
            if(nestedJsonObject.has("city"))
                city = nestedJsonObject.getString("city");
            if(nestedJsonObject.has("postcode"))
                postcode = nestedJsonObject.getInt("postcode");
            if(nestedJsonObject.has("district"))
                district = nestedJsonObject.getString("district");
            if(nestedJsonObject.has("contact"))
                contact = nestedJsonObject.getString("contact");

            District district_ob = new District(district);
            Address address = new Address(l1, l2, district_ob, city, postcode);

            h = new Hotel(dataSource,hotel_group_id,h_name,email,address,contact,
                    city,postcode,is_active);
        }
        return h;
    }
}
