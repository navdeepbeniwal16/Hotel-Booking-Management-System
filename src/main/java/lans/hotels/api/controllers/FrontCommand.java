package lans.hotels.api.controllers;

import lans.hotels.api.Responder;
import lans.hotels.api.entrypoint.IFrontCommand;
import lans.hotels.api.auth.Auth0Adapter;
import lans.hotels.api.exceptions.CommandException;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user.Role;
import lans.hotels.use_cases.UseCase;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public abstract class FrontCommand implements IFrontCommand  {
    protected IDataSource dataSource;
    protected ServletContext context;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String method;
    protected Auth0Adapter auth;
    protected UseCase useCase;
    protected DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    protected JSONObject requestBody = new JSONObject();
    protected int statusCode;

    protected String[] commandPath;
    protected Responder responder;

    public void init(ServletContext context,
                     HttpServletRequest request,
                     HttpServletResponse response,
                     IDataSource dataSource,
                     Responder responder) {
        this.context = context;
        this.request = request;
        this.method = request.getMethod();
        this.response = response;
        this.dataSource = dataSource;
        this.responder = responder;
        this.auth = Auth0Adapter.getAuthorization(request);
        this.commandPath = request.getPathInfo().split("/");
    }

    abstract protected void concreteProcess() throws Exception;
    public void process() throws ServletException, IOException, CommandException, SQLException {
        try {
            parseRequestBody();
            concreteProcess();
        } catch (Exception e) {
            responder.internalServerError();
        }

    }

    protected void parseRequestBody() throws IOException {
        BufferedReader requestReader = request.getReader();
        String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject body = new JSONObject();
        if (lines.length() > 0) {
            body = new JSONObject(lines);
        }
        requestBody = body;
    }

    protected void checkCommandPath(Integer exactLength) {
        assert commandPath.length == exactLength;
    }

    protected void checkCommandPath(Integer minLength, Integer maxLength) {
        assert commandPath.length >= minLength;
        assert commandPath.length <= maxLength;
    }


    protected <T> T delegateToAuth(Role.Name role, Callable<T> handler) {
        try {
            if (role.equals(Role.Name.Admin)) return auth.asAdmin(handler, responder::unauthorized);
            if (role.equals(Role.Name.Hotelier)) return auth.asHotelier(handler, responder::unauthorized);
            if (role.equals(Role.Name.Customer)) return auth.asCustomer(handler, responder::unauthorized);
            responder.internalServerError();
        } catch (Exception e) {
            responder.internalServerError();
        }
        return null;
    }


    protected <T> T asAdmin(Callable<T> handler) {
        return delegateToAuth(Role.Name.Admin, handler);
    }

    protected <T> T asHotelier(Callable<T> handler) {
        return delegateToAuth(Role.Name.Hotelier, handler);
    }
    protected <T> T asCustomer(Callable<T> handler) {
        return delegateToAuth(Role.Name.Admin, handler);
    }
}
