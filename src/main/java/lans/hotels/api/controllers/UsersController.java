package lans.hotels.api.controllers;

import lans.hotels.datasource.search_criteria.UserSearchCriteria;

import lans.hotels.domain.user_types.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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
            ArrayList<User> users;
            switch (request.getMethod()) {
                case HttpMethod.GET:
                case HttpMethod.POST:
                    JSONObject body = getRequestBody(request);
                    if (body.has("search")) {
                        UserSearchCriteria criteria = new UserSearchCriteria();
                        JSONObject searchQueryBody = body.getJSONObject("search");

                        if (searchQueryBody.has("id")) {
                            Integer userID = searchQueryBody.getInt("id");
                            if (userID != null) criteria.setId(userID);
                        }

                        try {
                            users = dataSource.findBySearchCriteria(User.class, criteria);
                        } catch (Exception e) {
                            System.err.println("POST /api/customers group: " + Arrays.toString(commandPath));
                            System.err.println("POST /api/customers group: " + e.getMessage());
                            System.err.println("POST /api/customers group: " + e.getClass());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                            return;
                        }

                        returnUserJSON(users);
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

    public void returnUserJSON(ArrayList<User> users) throws IOException {
        JSONArray userArray = new JSONArray();
        PrintWriter out = response.getWriter();
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject aUser;
        for (User user: users) {

            aUser = new JSONObject();
            aUser.put("id", user.getId());
            aUser.put("role", user.getRole());
            userArray.put(aUser);
        }
        JSONObject customerJson = new JSONObject();
        customerJson.put("result", userArray);
        out.print(customerJson);
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

}

