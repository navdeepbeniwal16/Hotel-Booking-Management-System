package lans.hotels.controllers;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.SessionUtils;
import com.auth0.Tokens;
import lans.hotels.api.IFrontCommand;
import lans.hotels.api.exceptions.CommandException;
import lans.hotels.domain.IDataSource;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

public abstract class FrontCommand implements IFrontCommand  {
    protected IDataSource dataSource;
    protected ServletContext context;
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    public void init(ServletContext context,
                     HttpServletRequest request,
                     HttpServletResponse response,
                     IDataSource dataSource) {
        this.context = context;
        this.request = request;
        this.response = response;
        this.dataSource = dataSource;
    }

    public void process() throws ServletException, IOException, CommandException, SQLException {
        if (context == null || request == null || response == null || dataSource == null) {
            throw new CommandException(this.getClass() + " must be initialised by it can process a command.");
        }
        concreteProcess();
    }

    abstract protected void concreteProcess() throws CommandException, IOException, SQLException;

    public JSONObject getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader requestReader = request.getReader();

        String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject body;
        if (lines.length() > 0) {
            body = new JSONObject(lines);
        } else {
            return null;
        }
        return body;
    }
}
