package lans.hotels.api;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Responder {
    HttpServletResponse response;
    public Responder(HttpServletResponse response) {
        this.response = response;
    }

    public void error(String errorMessage, int statusCode) {
        JSONObject errorBody = new JSONObject();
        errorBody.put("errorMessage", errorMessage);
        respond(errorBody, statusCode);
    }

    public void respond(JSONObject responseBody, int statusCode) {
        try {
            response.setStatus(statusCode);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.println(responseBody);
            out.flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
        }
    }

    public void unimplemented(String message) {
        error(message, HttpServletResponse.SC_NOT_IMPLEMENTED);
    }

    public Void unauthorized() {
        error("Unauthorized", HttpServletResponse.SC_UNAUTHORIZED);
        return null;
    }

    public Void internalServerError() {
        error("Something went wrong!", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return null;
    }
}
