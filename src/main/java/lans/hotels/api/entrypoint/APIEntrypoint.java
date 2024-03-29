package lans.hotels.api.entrypoint;

import lans.hotels.api.auth.AuthorizationFactory;
import lans.hotels.api.controllers.UnknownController;
import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.datasource.facade.PostgresFacade;
import lans.hotels.datasource.connections.DBConnection;
import lans.hotels.domain.IDataSource;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.util.*;

@WebServlet(name = "APIFrontController", value = "/api/*")
public class APIEntrypoint extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        handleRequest(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        handleRequest(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        IDataSource dataSourceLayer = null;
        DBConnection connectionFactory = null;
        try {
            connectionFactory = (DBConnection) getServletContext().getAttribute("connectionFactory");
            if (connectionFactory == null) {
                throw new Exception("no connection factory");
            }

            dataSourceLayer = PostgresFacade.newInstance(request.getSession(true), connectionFactory.getConnection());
            if (!dataSourceLayer.isOpen()) {
                throw new Exception("no connections available");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (dataSourceLayer != null && dataSourceLayer.isOpen()) {
                    dataSourceLayer.commit();
                }
            } catch (DataSourceLayerException ex) {
                throw new RuntimeException(ex);
            }
            throw new ServletException("APIEntrypoint.handleRequest():\n" + "Method:\t" + request.getMethod() + "\nError message:\t" + e);
        }
        try {
            handleCommand(dataSourceLayer, request, response);
        } catch (Exception e) {
            System.out.println("APIFrontController.handleRequest(): " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("APIEntrypoint.handleRequest():\n" + "Method:\t" + request.getMethod() + "\nError message:\t" + e);
        } finally {
            try {
                if (dataSourceLayer.isOpen()) {
                    dataSourceLayer.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServletException("APIEntrypoint.handleRequest():\n" + "Method:\t" + request.getMethod() + "\nError message:\t" + e);
            }
        }
    }

    private void handleCommand(IDataSource dataSource, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        // Dynamically instantiate the appropriate controller
        IFrontCommand command = getCommandWithAuth(request, dataSource);
        command.init(getServletContext(), request, response, dataSource);
        // Execute the controller
        command.process();
    }

    private IFrontCommand getCommandWithAuth(HttpServletRequest request, IDataSource dataSource) throws ServletException {
        try {
            AuthorizationFactory authFactory = (AuthorizationFactory) getServletContext().getAttribute("AuthFactory");
            authFactory.injectAuthorization(request, dataSource);
            String[] commandPath = request.getPathInfo().split("/");
            return (IFrontCommand) getCommandClass(commandPath).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.out.println("APIFrontController.getCommand(): " + e.getMessage());
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        }
    }

    private Class getCommandClass(String[] commandPath) {
        if (commandPath.length == 0) return UnknownController.class;
        Class result;
        final String commandClassName = "lans.hotels.api.controllers." +
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
}