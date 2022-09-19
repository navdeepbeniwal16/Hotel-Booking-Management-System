package lans.hotels.controllers;

import lans.hotels.api.HttpMethod;
import lans.hotels.api.exceptions.CommandException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class RoomsController extends FrontCommand {

    @Override
    protected void concreteProcess() throws CommandException, IOException {
        switch(request.getMethod()) {
            case HttpMethod.GET:
                String[] commandPath = request.getPathInfo().split("/");


                if (commandPath.length == 3) {
                    // GET /api/rooms/:id
                    Integer id;
                    try {
                        id = Integer.parseInt(commandPath[2]);
                        System.err.println("PUT /rooms/:id: NOT IMPLEMENTED");
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        return;
                    } catch (NumberFormatException e) {
                        System.err.println("GET /api/rooms/:id: " + Arrays.toString(commandPath));
                        System.err.println("GET /api/rooms/:id: " + e.getMessage());
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                        return;
                    }
                } else if (commandPath.length==2) {

                } else {
                    System.err.println("GET /rooms: bad request");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            case HttpMethod.POST:
                System.err.println("POST /rooms: NOT IMPLEMENTED");
                return;
            case HttpMethod.PUT:
                System.err.println("PUT /rooms: NOT IMPLEMENTED");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            case HttpMethod.DELETE:
                System.err.println("DELETE /rooms: bad request");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
        }

    }
}
