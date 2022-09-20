package lans.hotels.controllers;

import lans.hotels.api.exceptions.CommandException;
import lans.hotels.api.HttpMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class AuthController extends FrontCommand {
    /**
     * 1. Browser requests resource
     * 2. Sever determines resource requires AUTH
     * 3. If not logged in or authorised, send back 401 UNAUTHORISED
     * 4. Browser receives 401 and asks user to login (username and password)
     * 5. Browser asks for resource again but this time with security HTTP header, username and password
     * 6. Container verifies username and password
     * 7. If valid, return resource
     *
     * @throws CommandException
     * @throws IOException
     * @throws SQLException
     */
    @Override
    protected void concreteProcess() throws CommandException, IOException, SQLException {
        switch (request.getMethod()) {
            case HttpMethod.GET:
                System.out.println("GET /api/auth NOT IMPLEMENTED");
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            case HttpMethod.POST:
                System.out.println("POST /api/auth NOT IMPLEMENTED");
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            case HttpMethod.PUT:
                System.out.println("PUT /api/auth NOT IMPLEMENTED");
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            case HttpMethod.DELETE:
                System.out.println("DELETE /api/auth NOT IMPLEMENTED");
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            default:
                System.out.println("/api/auth invalid HTTP method: " + request.getMethod());
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }
}
