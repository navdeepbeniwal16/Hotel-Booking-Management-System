package lans.hotels.api;


import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lans.hotels.controllers.UnknownController;
import lans.hotels.datasource.facade.PostgresFacade;
import lans.hotels.datasource.connections.DBConnection;
import lans.hotels.domain.IDataSource;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

import java.security.interfaces.RSAPublicKey;
import java.util.*;

@WebServlet(name = "APIFrontController", value = "/api/*")
public class APIFrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
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
            handleAuth(request);
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

    protected void handleAuth(HttpServletRequest request) {
        String namespace = "lans_hotels/";
        try {
            String headerString = request.getHeader("Authorization");
            if (headerString == null || headerString.equals("")) {
                System.out.println("APIFrontController.handleAuth(): " + request.getRequestURI() + " | null Authorization header");
                request.getSession().setAttribute("auth", false);
                return;
            }

            String authorizationType = headerString.split(" ")[0];
            if (!authorizationType.equals("Bearer")) {
                System.out.println("APIFrontController.handleAuth(): Invalid authorisation type: " + authorizationType);
                request.getSession().setAttribute("auth", false);
                return;
            }

            String tokenString = headerString.split(" ")[1];
            DecodedJWT decodedJwt = JWT.decode(tokenString);
            JwkProvider jwkProvider = (JwkProvider) getServletContext().getAttribute("jwkProvider");
            Jwk jwk = jwkProvider.get(decodedJwt.getKeyId());
            RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("https://dev-easqepri.us.auth0.com/")
                    .withAudience()
                    .build();
            DecodedJWT jwt = verifier.verify(tokenString);
            request.getSession().setAttribute("auth", true);
            Base64.Decoder decoder = Base64.getUrlDecoder();
            JSONObject payload = new JSONObject(new String(decoder.decode(jwt.getPayload())));
            System.out.println("PAYLOAD:\n\t" + payload);
            String email = (String) payload.get(namespace + "email");
            JSONArray rolesArray = (JSONArray) payload.get(namespace + "roles");
            ArrayList<String> roles = new ArrayList<>();
            for(Object role: rolesArray) roles.add((String) role);

            if (payload.has("email")) request.getSession().setAttribute("email", email);
            if (payload.has("roles")) request.getSession().setAttribute("roles", roles);

            System.out.println("AUTHORISATION:\n\t" + email + "\n\t" + roles.toString());
        } catch (Exception e) {
            System.err.println("AUTH ERROR - APIFrontController:\n\t" + e);
            e.printStackTrace();
            request.getSession().setAttribute("auth", false);
        }
    }
}