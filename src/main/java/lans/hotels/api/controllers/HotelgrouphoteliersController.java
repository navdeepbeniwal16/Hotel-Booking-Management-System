package lans.hotels.api.controllers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.hotel_group.HotelGroupHotelier;
import lans.hotels.domain.user_types.Role;
import lans.hotels.use_cases.CreateHotel;
import lans.hotels.use_cases.OnboardHotelier;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class HotelgrouphoteliersController extends FrontCommand {
    @Override
    protected void concreteProcess() throws IOException, SQLException {
        String[] commandPath = request.getPathInfo().split("/");
        int statusCode;

        switch(request.getMethod()) {
            case HttpMethod.GET:
                if (commandPath.length == 3) {
                    // GET /api/hotels/:id
                    Integer id;
                    try {
                        id = Integer.parseInt(commandPath[2]);
                    } catch (NumberFormatException e) {
                        System.err.println("GET /api/hotelgrouphoteliers/:id: " + Arrays.toString(commandPath));
                        System.err.println("GET /api/hotelgrouphoteliers/:id: " + e.getMessage());
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
                    } else {
                        ArrayList<HotelGroupHotelier> hg_hoteliers;
                        JSONArray hotelArray = new JSONArray();
                        PrintWriter out = response.getWriter();
                        JSONObject aHGHotelier;
                        JSONObject hotelJson = new JSONObject();
                        try {
                            hg_hoteliers = dataSource.findAll(HotelGroupHotelier.class);
                            response.setStatus(200);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");


                            for (HotelGroupHotelier hotelGroupHotelier: hg_hoteliers) {
                                aHGHotelier = new JSONObject();
                                aHGHotelier.put("id", hotelGroupHotelier.getId());
//                                aHGHotelier.put("hotelier_id", hotelGroupHotelier.getHotelierId());
//                                aHGHotelier.put("hotel_group_id", hotelGroupHotelier.getHotelGroupId());
                                hotelArray.put(aHGHotelier);
                            }

                            hotelJson.put("result", hotelArray);
                            out.print(hotelJson);
                            out.flush();
                            return;
                        } catch (Exception e) {
                            System.err.println("GET /api/hotelgrouphoteliers: " + Arrays.toString(commandPath));
                            e.printStackTrace();
                            System.err.println("GET /api/hotelgrouphoteliers: " + e.getMessage());
                            System.err.println("GET /api/hotelgrouphoteliers: " + e.getClass());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                            return;
                        }
                    }
                } else {
                    System.err.println("Hotelgrouphotelier contoller: " + Arrays.toString(commandPath));
                    System.err.println("Hotelgrouphotelier: commandPath.length = " + commandPath.length);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                    return;
                }

                return;
            case HttpMethod.POST:
            case HttpMethod.PUT:
                return;
            case HttpMethod.DELETE:
            {
                if (!auth.isAdmin()) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    JSONObject error = new JSONObject();
                    error.put("message", HttpServletResponse.SC_UNAUTHORIZED + ": unauthorized");
                    PrintWriter out = response.getWriter();
                    out.println(error);
                    out.flush();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                String[] commandPath2 = request.getPathInfo().split("/");
                int id = -1;
                if (commandPath2.length == 2) {

                    JSONObject body = getRequestBody(request);
                    System.out.println("Parsed Hotel Group JSONObject : " + body);

                    if(body.has("hotel_group_hotelier")) {
                        JSONObject nestedJsonObject = body.getJSONObject("hotel_group_hotelier");

                        if (nestedJsonObject.has("id"))
                            id = nestedJsonObject.getInt("id");
                    }

                    PrintWriter out = response.getWriter();
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    JSONObject returnBody;
                    returnBody = new JSONObject();

                    if(id==-1)
                        returnBody.put("deleted", false);

                    JSONObject hgJSON = new JSONObject();
                    try{
                        HotelGroupHotelier hotelGroupHotelier = new HotelGroupHotelier(id, dataSource);
                        hotelGroupHotelier.remove();
                        dataSource.commit();
                        returnBody.put("deleted", true);
                    } catch (Exception e) {
                        returnBody.put("deleted", false);
                        System.err.println("DELETE /api/hotelgrouphotleiers: " + Arrays.toString(commandPath2));
                        e.printStackTrace();
                        System.err.println("DELETE /api/hotelgrouphotleiers: " + e.getMessage());
                        System.err.println("DELETE /api/hotelgrouphotleiers: " + e.getClass());
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                    } finally {
                        hgJSON.put("result", returnBody);
                        out.print(hgJSON);
                        out.flush();
                        return;
                    }
                }
                else {
                    System.err.println("Hotel Group controller: " + Arrays.toString(commandPath2));
                    System.err.println("Hotel Group controller: commandPath2.length = " + commandPath2.length);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                    return;
                }
            }
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        }
    }

    private void getHotelGroup(Integer id) throws IOException, SQLException {

        HotelGroupHotelier hotelGroupHotelier = dataSource.find(HotelGroupHotelier.class, id);

        if (hotelGroupHotelier == null) {
            System.out.println("404 NOT FOUND");
            response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        } else {
            JSONObject json = new JSONObject();
            PrintWriter out = response.getWriter();
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
//            json.put("id", hotelGroupHotelier.getId());
//            json.put("hotelier_id", hotelGroupHotelier.getHotelierId());
//            json.put("hotel_group_id", hotelGroupHotelier.getHotelGroupId());
            out.print(json);
            out.flush();
        }
    }
}
