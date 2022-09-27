package lans.hotels.api.controllers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.UserSearchCriteria;

import lans.hotels.domain.hotel_group.HotelGroupHotelier;
import lans.hotels.domain.user_types.Role;
import lans.hotels.domain.user_types.User;
import lans.hotels.use_cases.GetAllHoteliers;
import lans.hotels.use_cases.GetAllUsers;
import lans.hotels.use_cases.OnboardHotelier;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Arrays;

public class UsersController extends FrontCommand {
    @Override
    protected void concreteProcess() throws IOException {
        String[] commandPath = request.getPathInfo().split("/");
        if (commandPath.length != 2) {
            System.err.println("Customers controller: " + Arrays.toString(commandPath));
            System.err.println("Customers controller: commandPath.length = " + commandPath.length);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
            return;

        } else {
            int statusCode;
            switch (request.getMethod()) {
                case HttpMethod.GET:
                    if (!auth.isAdmin()) {
                        sendUnauthorizedJsonResponse(response);
                        return;
                    }
                    useCase = new GetAllUsers(dataSource);
                    useCase.execute();
                    statusCode = useCase.succeeded() ?
                            HttpServletResponse.SC_OK :
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                    sendJsonResponse(response, useCase.getResult(), statusCode);
                    return;
                case HttpMethod.POST:

                    JSONObject body = getRequestBody(request);
                    if (body.has("search")) {
                        UserSearchCriteria criteria = new UserSearchCriteria();
                        JSONObject searchQueryBody = body.getJSONObject("search");

                        if (searchQueryBody.has("id")) {
                            Integer userID = searchQueryBody.getInt("id");
                            if (userID != null) criteria.setId(userID);
                        }
                        if (searchQueryBody.has("name")) {
                            String name = searchQueryBody.getString("name");
                            if (name != null) criteria.setName(name);
                        }
                        if (searchQueryBody.has("email")) {
                            String email = searchQueryBody.getString("email");
                            if (email != null) criteria.setEmail(email);
                        }
                        if (searchQueryBody.has("role")) {
                            Integer role = searchQueryBody.getInt("role");
                            if (role != null) criteria.setRole(role);
                        }

                        String userType = "";
                        if (searchQueryBody.has("type")) {
                            userType = searchQueryBody.getString("type");

                        }

                        if (userType.equals("hotelier")) {
                            if (!auth.isAdmin()) {
                                sendUnauthorizedJsonResponse(response);
                                return;
                            }
                            useCase = new GetAllHoteliers(dataSource);
                        } else {
                            useCase = new GetAllUsers(dataSource);
                        }

                        useCase.execute();
                        statusCode = useCase.succeeded() ?
                                HttpServletResponse.SC_OK :
                                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                        sendJsonResponse(response, useCase.getResult(), statusCode);
                        return;

                    }
                    else if (body.has("hotelier")) {
                        User user = getUserFromJSONObject(body);

                        if (user == null)
                            throw new InvalidObjectException("Failed to parse hotelier object from request body");

                        if (!auth.isAdmin()) {
                            sendUnauthorizedJsonResponse(response);
                            return;
                        }
                        useCase = new OnboardHotelier(dataSource);
                        useCase.execute();
                        statusCode = useCase.succeeded() ?
                                HttpServletResponse.SC_OK :
                                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                        sendJsonResponse(response, useCase.getResult(), statusCode);
                        return;

                    }
                    else
                    {
                        System.err.println("POST /api/hoteliers: " + Arrays.toString(commandPath));
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                        return;
                    }

                case HttpMethod.PUT:

                case HttpMethod.DELETE:
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
            }
        }
    }
    public User getUserFromJSONObject(JSONObject body) {

        User user = null;
        String email = null;

        if(body.has("hotelier")) {
            JSONObject nestedJsonObject = body.getJSONObject("hotelier");

            if(nestedJsonObject.has("email"))
                email = nestedJsonObject.getString("email");

            Role role = null;
            try {
                role = new Role(2);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                user = new User(dataSource,null,email,null,role,null,null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

}

