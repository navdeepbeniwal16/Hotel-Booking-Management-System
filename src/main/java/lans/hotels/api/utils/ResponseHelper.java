package lans.hotels.api.utils;

import lans.hotels.domain.IDataSource;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseHelper {
    HttpServletResponse response;
    IDataSource dataSource;
    public ResponseHelper(HttpServletResponse response, IDataSource dataSource) {
        this.response = response;
        this.dataSource = dataSource;
    }

    public Void error(String errorMessage, int statusCode) {
        JSONObject errorBody = new JSONObject();
        errorBody.put("error", errorMessage);
        respond(errorBody, statusCode);
        return null;
    }

    public Void respondOK(JSONObject responseBody) {
        return respond(responseBody, HttpServletResponse.SC_OK);
    }

    public Void respond(JSONObject responseBody, int statusCode) {
        try {
            response.setStatus(statusCode);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.println(responseBody);
            out.flush();
            dataSource.commit();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
        }
        return null;
    }

    public Void unimplemented(String message) {
        error(message, HttpServletResponse.SC_NOT_IMPLEMENTED);
        return null;
    }

    public Void unauthorized() {
        error("Unauthorized", HttpServletResponse.SC_UNAUTHORIZED);
        return null;
    }

    public Void internalServerError() {
        return internalServerError("Something went wrong!");
    }

    public Void internalServerError(String message) {
        error(message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return null;
    }
}
