package lans.hotels.api.controllers;

import lans.hotels.api.entrypoint.IFrontCommand;
import lans.hotels.api.auth.Auth0Adapter;
import lans.hotels.api.exceptions.CommandException;
import lans.hotels.domain.IDataSource;
import lans.hotels.use_cases.UseCase;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.stream.Collectors;

public abstract class FrontCommand implements IFrontCommand  {
    protected IDataSource dataSource;
    protected ServletContext context;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected Auth0Adapter auth;
    protected UseCase useCase;
    protected DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    protected JSONObject requestBody = new JSONObject();

    public void init(ServletContext context,
                     HttpServletRequest request,
                     HttpServletResponse response,
                     IDataSource dataSource) {
        this.context = context;
        this.request = request;
        this.response = response;
        this.dataSource = dataSource;
        this.auth = Auth0Adapter.getAuthorization(request);
    }

    public void process() throws ServletException, IOException, CommandException, SQLException {
        if (context == null || request == null || response == null || dataSource == null) {
            throw new CommandException(this.getClass() + " must be initialised by it can process a command.");
        }
        requestBody = getRequestBody(request);
        concreteProcess();
    }

    abstract protected void concreteProcess() throws CommandException, IOException, SQLException;

    protected JSONObject getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader requestReader = request.getReader();
        String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject body = new JSONObject();
        if (lines.length() > 0) {
//            System.out.println("FrontCommand.getRequestBody():\n" + lines);
            body = new JSONObject(lines);
        }
        return body;
    }

    protected void sendJsonResponse(HttpServletResponse response, JSONObject responseBody, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(responseBody);
        out.flush();
    }

    protected void sendJsonErrorResponse(HttpServletResponse response, String errorMessage, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject errorBody = new JSONObject();
        errorBody.put("errorMessage", errorMessage);
        out.println(errorBody);
        out.flush();
    }

    protected void sendUnauthorizedJsonResponse() throws IOException {
        JSONObject errorObject = new JSONObject().put("message", "unauthorized");
        sendJsonResponse(response, errorObject, HttpServletResponse.SC_UNAUTHORIZED);
    }
}
