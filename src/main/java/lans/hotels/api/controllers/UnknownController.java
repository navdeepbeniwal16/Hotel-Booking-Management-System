package lans.hotels.api.controllers;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UnknownController extends FrontCommand {
    @Override
    public void concreteProcess() throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Error: unknown command " + request.getParameter("command"));
        out.println(jsonObject);
        out.flush();
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
