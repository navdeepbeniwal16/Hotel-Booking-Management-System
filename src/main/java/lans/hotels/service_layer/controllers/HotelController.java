package lans.hotels.controllers;

import lans.hotels.api.HttpMethod;
import lans.hotels.domain.hotel.Hotel;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class HotelController extends FrontCommand {
    @Override
    protected void concreteProcess() throws IOException {
        switch(request.getMethod()) {
            case HttpMethod.GET:
                getHotel();
                return;
            case HttpMethod.PUT:
            case HttpMethod.POST:
            case HttpMethod.DELETE:
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        }
    }

    private void getHotel() throws IOException {
        // /api/hotel/:id
        String[] commandPath = request.getPathInfo().split("/");
        if (commandPath.length != 3) { // TODO: is this causing the 500? #bug
            System.err.println("getHotel(): " + Arrays.toString(commandPath));
            System.err.println("getHotel(): commandPath.length = " + commandPath.length);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
            return;
        }

        Integer id;
        try {
            id = Integer.parseInt(commandPath[2]);
        } catch (NumberFormatException e) {
            System.err.println("getHotel(): " + Arrays.toString(commandPath));
            System.err.println("getHotel(): " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
            return;
        }

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
