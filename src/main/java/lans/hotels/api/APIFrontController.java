package lans.hotels.api;

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
}
