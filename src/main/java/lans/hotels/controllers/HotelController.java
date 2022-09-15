package lans.hotels.controllers;

import lans.hotels.api.HttpMethod;
import lans.hotels.domain.hotel.Hotel;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
        if (commandPath.length != 1) { // TODO: is this causing the 500? #bug
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
            return;
        }

        Integer id;
        try {
            id = Integer.parseInt(commandPath[1]);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
            return;
        }

        Hotel hotel = (Hotel) dataSource.find(Hotel.class, id);

        if (hotel == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        } else {
            // TODO: marshall response!
            JSONObject json = new JSONObject();
            PrintWriter out = response.getWriter();
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            json.put("id", hotel.getId()); // " { id: ... }
            json.put("name", hotel.getName());
            json.put("email", hotel.getEmail());
            out.print(json.toString());
            out.flush();
        }
    }
}
