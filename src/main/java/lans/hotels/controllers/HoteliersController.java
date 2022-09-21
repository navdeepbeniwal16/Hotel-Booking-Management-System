package lans.hotels.controllers;

import lans.hotels.api.HttpMethod;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.HotelGroupSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelierSearchCriteria;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.user_types.Hotelier;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.PrintWriter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.stream.Collectors;

public class HoteliersController extends FrontCommand{

    @Override
    protected void concreteProcess() throws IOException {
        String[] commandPath = request.getPathInfo().split("/");

        if (commandPath.length!=2) {
            System.err.println("Hoteliers controller: " + Arrays.toString(commandPath));
            System.err.println("Hoteliers controller: commandPath.length = " + commandPath.length);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
            return;
        }
        else{
            ArrayList<Hotelier> hoteliers;
            switch(request.getMethod()) {
                case HttpMethod.GET:
                    try {
                        hoteliers = (ArrayList<Hotelier>) dataSource.findAll(Hotelier.class);
                    } catch (Exception e) {
                        System.err.println("GET /api/hoteliers: " + Arrays.toString(commandPath));
                        System.err.println("GET /api/hoteliers: " + e.getMessage());
                        System.err.println("GET /api/hoteliers: " + e.getClass());
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                        return;
                    }

                    returnHotelierJSON(hoteliers);
                    return;
                case HttpMethod.POST:
                {
                    JSONObject body = getRequestBody(request);

                    if (body.has("search")) {
                        HotelierSearchCriteria criteria = new HotelierSearchCriteria();
                        JSONObject searchQueryBody = body.getJSONObject("search");
                        if (searchQueryBody.has("id")) {
                            Integer hotelierId = searchQueryBody.getInt("id");
                            if (hotelierId != null) criteria.setId(hotelierId);
                        }
                        if (searchQueryBody.has("name")) {
                            String hotelierName = searchQueryBody.getString("name");
                            if (hotelierName != null) criteria.setName(hotelierName);
                        }
                        if (searchQueryBody.has("email")) {
                            String email = searchQueryBody.getString("email");
                            if (email != null) criteria.setEmail(email);
                        }
                        if (searchQueryBody.has("is_active")) {
                            Boolean is_active = searchQueryBody.getBoolean("is_active");
                            if (is_active != null) criteria.setIsActive(is_active);
                        }

                        try {
                            hoteliers = dataSource.findBySearchCriteria(Hotelier.class, criteria);
                        } catch (Exception e) {
                            System.err.println("POST /api/hoteliers: " + Arrays.toString(commandPath));
                            System.err.println("POST /api/hoteliers: " + e.getMessage());
                            System.err.println("POST /api/hoteliers: " + e.getClass());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                            return;
                        }

                        returnHotelierJSON(hoteliers);
                        return;
                    }
                    else if (body.has("hotelier")) {

                        Hotelier hotelier = null;

                        hotelier = getHotelierFromJsonObject(body);

                        if(hotelier == null)
                                throw new InvalidObjectException("Failed to parse hotelier object from request body");

                        System.out.println("Parsed Hotel Object : " + hotelier);

                        boolean success;
                        try{
                            success = dataSource.insert(Hotelier.class,hotelier);
                        } catch (Exception e) {
                            System.err.println("POST /api/hoteliers: " + Arrays.toString(commandPath));
                            System.err.println("POST /api/hoteliers: " + e.getMessage());
                            System.err.println("POST /api/hoteliers: " + e.getClass());
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
                    else
                    {
                        System.err.println("POST /api/hotelgroups: " + Arrays.toString(commandPath));
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

    public void returnHotelierJSON(ArrayList<Hotelier> hoteliers) throws IOException {
        JSONArray hotelierArray = new JSONArray();
        PrintWriter out = response.getWriter();
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject aHotelier;
        for (Hotelier hotelier: hoteliers) {
            aHotelier = new JSONObject();
            aHotelier.put("hotelier_id", hotelier.getHotelierID());
            aHotelier.put("name",hotelier.getName());
            aHotelier.put("email",hotelier.getEmail());
            aHotelier.put("user_id", hotelier.getUserID());
            aHotelier.put("isActive", hotelier.getStatus());
            hotelierArray.put(aHotelier);
        }

        JSONObject hotelierJson = new JSONObject();
        hotelierJson.put("result", hotelierArray);
        out.print(hotelierJson);
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

    public Hotelier getHotelierFromJsonObject(JSONObject jsonObject) {

        Hotelier hotelier = null;
        String name = "";
        String email = "";
        String password = "";
        if(jsonObject.has("hotelier")) {
            JSONObject nestedJsonObject = jsonObject.getJSONObject("hotelier");

            if(nestedJsonObject.has("name"))
                name = nestedJsonObject.getString("name");
            if(nestedJsonObject.has("email"))
                email = nestedJsonObject.getString("email");
            if(nestedJsonObject.has("password"))
                password = nestedJsonObject.getString("password");

            hotelier = new Hotelier(dataSource,name,email,password,2,true);
        }
        return hotelier;
    }
}
