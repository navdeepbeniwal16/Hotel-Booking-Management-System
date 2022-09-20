package lans.hotels.controllers;

import lans.hotels.api.HttpMethod;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.user_types.Hotelier;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.stream.Collectors;

public class HoteliersController extends FrontCommand{

    @Override
    protected void concreteProcess() throws IOException {
        switch(request.getMethod()) {
            case HttpMethod.GET:
                String[] commandPath = request.getPathInfo().split("/");


                if (commandPath.length==2) {

                    BufferedReader requestReader = request.getReader();

                    String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
                    System.out.println(lines);
                    JSONObject body = new JSONObject();

                    if (lines.length() > 0) {
                        System.out.println(lines);
                        body = new JSONObject(lines);
                    }

                    if (body.has("search")) {
                        // perform search
                    } else {
                        ArrayList<Hotelier> hoteliers;
                        try {
                            hoteliers = (ArrayList<Hotelier>) dataSource.findAll(Hotelier.class);
                        } catch (Exception e) {
                            System.err.println("GET /api/hoteliers: " + Arrays.toString(commandPath));
                            System.err.println("GET /api/hoteliers: " + e.getMessage());
                            System.err.println("GET /api/hoteliers: " + e.getClass());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                            return;
                        }

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
                } else {
                    System.err.println("Hoteliers controller: " + Arrays.toString(commandPath));
                    System.err.println("Hoteliers controller: commandPath.length = " + commandPath.length);
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


    public void getHotelier(Integer id) throws IOException, SQLException {
        Hotelier hotelier = (Hotelier) dataSource.find(Hotelier.class, id);

        if (hotelier == null) {
            System.out.println("404 NOT FOUND");
            System.out.println("\tHotelierController.getHotelier(): hotelier.id=" + id);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        } else {
            JSONObject json = new JSONObject();
            PrintWriter out = response.getWriter();
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            json.put("id", hotelier.getId());
            json.put("user_id", hotelier.getUserID());
            json.put("isActive", hotelier.getStatus());
            out.print(json);
            out.flush();
        }
    }
}
