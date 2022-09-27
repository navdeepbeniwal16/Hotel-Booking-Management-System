package lans.hotels.api.controllers;

import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.HotelGroupSearchCriteria;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.District;
import lans.hotels.use_cases.CreateHotel;
import lans.hotels.use_cases.CreateHotelGroup;
import lans.hotels.use_cases.GetAllHotelGroupDetails;
import lans.hotels.use_cases.GetSpecificHotelGroup;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class HotelgroupsController extends FrontCommand {
    @Override
    protected void concreteProcess() throws IOException, SQLException {
        ArrayList<HotelGroup> hotel_groups;
        String[] commandPath = request.getPathInfo().split("/");

        if(commandPath.length!=2) {
            System.err.println("HotelGroup controller: " + Arrays.toString(commandPath));
            System.err.println("HotelGroup controller: commandPath.length = " + commandPath.length);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
        }
        else {
            int statusCode;
            switch (request.getMethod()) {

                case HttpMethod.GET:
                    if (!auth.isAdmin()) {
                        sendUnauthorizedJsonResponse(response);
                        return;
                    }
                    useCase = new GetAllHotelGroupDetails(dataSource);
                    useCase.execute();
                    statusCode = useCase.succeeded() ?
                            HttpServletResponse.SC_OK :
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                    sendJsonResponse(response, useCase.getResult(), statusCode);
                    return;

                case HttpMethod.POST: {
                    JSONObject body = getRequestBody(request);
                    if (!auth.isAdmin()) {
                        sendUnauthorizedJsonResponse(response);
                        return;
                    }

                    if (body.has("search")) {
                        JSONObject searchQueryBody = body.getJSONObject("search");
                        if (searchQueryBody.has("id")) {
                            if (!auth.isAdmin()) {
                                sendUnauthorizedJsonResponse(response);
                                return;
                            }
                        Integer hotelGroupId = searchQueryBody.getInt("id");

                        useCase = new GetSpecificHotelGroup(dataSource, hotelGroupId);
                        useCase.execute();
                        statusCode = useCase.succeeded() ?
                                HttpServletResponse.SC_OK :
                                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                        sendJsonResponse(response, useCase.getResult(), statusCode);
                        return;
                        }
                    }
                    else if (body.has("hotel_group")) {
                        HotelGroup hg = getHotelGroupFromJsonObject(body);

                        if (hg == null)
                            throw new InvalidObjectException("Failed to parse hotel group object from request body");

                        if (!auth.isAdmin()) {
                            sendUnauthorizedJsonResponse(response);
                            return;
                        }
                        useCase = new CreateHotelGroup(dataSource);
                        useCase.execute();
                        statusCode = useCase.succeeded() ?
                                HttpServletResponse.SC_OK :
                                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                        sendJsonResponse(response, useCase.getResult(), statusCode);
                        return;
                    }
                    else if (body.has("hotel_group")) {
                        HotelGroup hg = getHotelGroupFromJsonObject(body);

                        if (hg == null)
                            throw new InvalidObjectException("Failed to parse hotel group object from request body");

                        boolean success;
                        try {
                            success = dataSource.insert(HotelGroup.class, hg);
                            System.out.println("HI");
                        } catch (Exception e) {
                            System.err.println("POST /api/hotelgroups: " + e.getMessage());
                            System.err.println("POST /api/hotelgroups: " + e.getClass());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                            return;
                        }
                        try {
                            dataSource.commit();
                        } catch (DataSourceLayerException e) {
                            e.printStackTrace();
                        }
                        PrintWriter out = response.getWriter();
                        response.setStatus(200);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JSONObject aHG;
                        aHG = new JSONObject();
                        if (success)
                            aHG.put("created", success);
                        else
                            aHG.put("created", success);

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

    public JSONObject getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader requestReader = request.getReader();

        String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject body;
        if (lines.length() > 0) {
            body = new JSONObject(lines);
        } else {
            return null;
        }
        return body;
    }

    public void returnHotelGroupJSON(ArrayList<HotelGroup> hotel_groups) throws IOException {
        JSONArray hgArray = new JSONArray();
        PrintWriter out = response.getWriter();
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject aHotel;
        for (HotelGroup hotelGroup : hotel_groups) {
            aHotel = new JSONObject();
            aHotel.put("hotel_group_id", hotelGroup.getId());
            aHotel.put("name", hotelGroup.getName());
            aHotel.put("address", hotelGroup.getAddress().toString());
            aHotel.put("district", hotelGroup.getPhone());
            hgArray.put(aHotel);
        }

        JSONObject hotelJson = new JSONObject();
        hotelJson.put("result", hgArray);
        out.print(hotelJson);
        out.flush();
        return;
    }

    public HotelGroup getHotelGroupFromJsonObject(JSONObject jsonObject) {

        HotelGroup hg = null;
        String hg_name = "";
        String l1 = "";
        String l2 = "";
        String city = "";
        int postcode = 0;
        String district = "";
        String phone = "";

        if(jsonObject.has("hotel_group")) {
            JSONObject nestedJsonObject = jsonObject.getJSONObject("hotel_group");

            if(nestedJsonObject.has("hg_name"))
                hg_name = nestedJsonObject.getString("hg_name");
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
            if(nestedJsonObject.has("phone"))
                phone = nestedJsonObject.getString("phone");

            District district_ob = new District(district);
            Address address = new Address(l1, l2, district_ob, city, postcode);

            try {
                hg = new HotelGroup(dataSource,hg_name,address,phone);
            } catch (UoWException e) {
                e.printStackTrace();
            }
        }
        return hg;
    }
}