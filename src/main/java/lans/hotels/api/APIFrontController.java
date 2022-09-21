package lans.hotels.api;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.SessionUtils;
import com.auth0.Tokens;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lans.hotels.controllers.UnknownController;
import lans.hotels.datasource.facade.PostgresFacade;
import lans.hotels.datasource.connections.DBConnection;
import lans.hotels.domain.IDataSource;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Arrays;

@WebServlet(name = "APIFrontController", value = "/api/*")
public class APIFrontController extends HttpServlet {
    private Tokens tokens;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            DBConnection database = (DBConnection) getServletContext().getAttribute("DBConnection");
            handleAuth(request, response);
            IDataSource dataSourceLayer = PostgresFacade.newInstance(request.getSession(true), database.connection());
            IFrontCommand command = getCommand(request);
            // Dynamically instantiate the appropriate controller
            command.init(getServletContext(), request, response, dataSourceLayer);
            // Execute the controller
            command.process();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServletException("doGet():" + e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            DBConnection database = (DBConnection) getServletContext().getAttribute("DBConnection");
            IDataSource dataSourceLayer = PostgresFacade.newInstance(request.getSession(true), database.connection());
            IFrontCommand command = getCommand(request);
            // Dynamically instantiate the appropriate controller
            command.init(getServletContext(), request, response, dataSourceLayer);
            // Execute the controller
            command.process();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServletException("doPost():" + e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            DBConnection database = (DBConnection) getServletContext().getAttribute("DBConnection");
            IDataSource dataSourceLayer = PostgresFacade.newInstance(request.getSession(true), database.connection());
            IFrontCommand command = getCommand(request);
            // Dynamically instantiate the appropriate controller
            command.init(getServletContext(), request, response, dataSourceLayer);
            // Execute the controller
            command.process();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServletException("doPut():" + e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            DBConnection database = (DBConnection) getServletContext().getAttribute("DBConnection");
            IDataSource dataSourceLayer = PostgresFacade.newInstance(request.getSession(true), database.connection());
            IFrontCommand command = getCommand(request);
            // Dynamically instantiate the appropriate controller
            command.init(getServletContext(), request, response, dataSourceLayer);
            // Execute the controller
            command.process();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServletException("doDelete():" + e);
        }
    }
    private IFrontCommand getCommand(HttpServletRequest request) throws ServletException {
        try {
            String[] commandPath = request.getPathInfo().split("/");
            return (IFrontCommand) getCommandClass(commandPath).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.out.println("getCommand():" + e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }

    private Class getCommandClass(String[] commandPath) {
        if (commandPath.length == 0) return UnknownController.class;
        Class result;
        final String commandClassName = "lans.hotels.controllers." +
                capitalise(commandPath[1]) + "Controller";
        try {
            result = Class.forName(commandClassName);
        } catch (ClassNotFoundException e) {
            result = UnknownController.class;
            System.out.println("404 NOT FOUND");
            System.out.println("\t" + e.getMessage());
            System.out.println("\tgetCommandClass()" + Arrays.toString(commandPath));

        }
        return result;
    }

    private String capitalise(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    protected void handleAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticationController authController = (AuthenticationController) getServletContext().getAttribute("AuthenticationController");
        try {
            // TODO: removing access token when session ended?
            System.out.println("handleAuth");
            System.out.println(request.getHeader("Authorization"));

            if (request.getHeader("Authorization") == null) {
                return;
            }

            tokens = authController.handle(request, response);
            if (tokens.getAccessToken() != null && tokens.getIdToken() != null) {
                DecodedJWT jwt = JWT.decode(tokens.getAccessToken());
                String emailJWT = ((Claim) jwt.getClaim("email")).asString();
                SessionUtils.set(request, "accessToken", tokens.getAccessToken());
                SessionUtils.set(request, "idToken", tokens.getIdToken());
                SessionUtils.set(request, "requestEmail", emailJWT);
                System.out.println("User email:");
                System.out.println(emailJWT);
                System.out.println("User access token:");
                System.out.println(tokens.getAccessToken());
                System.out.println("User ID token:");
                System.out.println(tokens.getIdToken());
            }
        } catch (IdentityVerificationException e) {
            System.out.println(e.getCode());
            e.printStackTrace();
            System.out.println("handleAuth: " + e);
            SessionUtils.set(request, "accessToken", null);
            SessionUtils.set(request, "idToken", null);
        }
    }
}
