package lans.hotels.controllers;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UnknownCommand extends FrontCommand {
    @Override
    public void process() throws ServletException, IOException {
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
