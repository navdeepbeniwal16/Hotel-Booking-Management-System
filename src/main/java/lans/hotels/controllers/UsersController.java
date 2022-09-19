package lans.hotels.controllers;

import lans.hotels.api.HttpMethod;
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

public class UsersController extends FrontCommand{

    @Override
    protected void concreteProcess() throws IOException {
        String[] commandPath;
        switch(request.getMethod()) {
            case HttpMethod.GET:
                commandPath = request.getPathInfo().split("/");

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
                        ArrayList<User> users;
                        try {
                            users = (ArrayList<User>) dataSource.findAll(User.class);
                        } catch (Exception e) {
                            System.err.println("GET /api/users: " + Arrays.toString(commandPath));
                            System.err.println("GET /api/users: " + e.getMessage());
                            System.err.println("GET /api/users: " + e.getClass());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                            return;
                        }

                        JSONArray userArray = new JSONArray();
                        PrintWriter out = response.getWriter();
                        response.setStatus(200);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JSONObject aUser;
                        for (User user: users) {
                            aUser = new JSONObject();
                            aUser.put("id", user.getId());
                            aUser.put("name", user.getName());
                            aUser.put("email", user.getEmail());
                            int userRole = user.getRole();
                            if(userRole == 2)
                                aUser.put("role", "Hotelier");
                            else if(userRole == 3)
                                aUser.put("role", "Customer");
                            userArray.put(aUser);
                        }

                        JSONObject hotelierJson = new JSONObject();
                        hotelierJson.put("result", userArray);
                        out.print(hotelierJson);
                        out.flush();
                        return;
                    }
                } else {
                    System.err.println("Users controller: " + Arrays.toString(commandPath));
                    System.err.println("Users controller: commandPath.length = " + commandPath.length);
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


//    public JSONObject getRequestBody(HttpServletRequest request) throws IOException {
//        BufferedReader requestReader = request.getReader();
//
//        String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
//        System.out.println("Request Body Lines + " + lines);
//        JSONObject body;
//        if (lines.length() > 0) {
//            System.out.println(lines);
//            body = new JSONObject(lines);
//        } else {
//            return null;
//        }
//        return body;
//    }
}
