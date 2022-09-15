package lans.hotels.controllers;

import lans.hotels.api.exceptions.CommandException;
import lans.hotels.api.HttpMethod;
import lans.hotels.domain.hotel.Hotel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HotelController extends FrontCommand {
    @Override
    protected void concreteProcess() throws CommandException, IOException {
        switch(request.getMethod()) {
            case HttpMethod.GET:
                getHotel();
                return;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        }
    }

    private void getHotel() throws IOException {
        String[] commandPath = request.getPathInfo().split("/");
        if (commandPath.length != 1) {
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
        }
    }
}
