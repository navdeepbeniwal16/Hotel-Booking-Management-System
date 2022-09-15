package lans.hotels.api;

import lans.hotels.controllers.UnknownController;
import lans.hotels.datasource.facade.PostgresFacade;
import lans.hotels.datasource.connections.DBConnection;
import lans.hotels.domain.IDataSource;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "APIFrontController", value = "/api/*")
public class APIFrontController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            DBConnection database = (DBConnection) getServletContext().getAttribute("DBConnection");
            IDataSource dataSourceLayer = PostgresFacade.newInstance(request.getSession(), database.connection());
            IFrontCommand command = getCommand(request);
            command.init(getServletContext(), request, response, dataSourceLayer);
            command.process();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServletException("doGet():" + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private IFrontCommand getCommand(HttpServletRequest request) throws ServletException {
        try {
            System.out.println("path: " + request.getPathInfo());
            System.out.println("split path:" + request.getPathInfo().split("/"));
            String[] commandPath = request.getPathInfo().split("/");
            return (IFrontCommand) getCommandClass(commandPath).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.out.println("getCommand():" + e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }

    private Class getCommandClass(String[] commandPath) {
        if (commandPath.length == 0) return UnknownController.class;
        System.out.println("command path: " + commandPath.toString());
        Class result;
        final String commandClassName = "lans.hotels.controllers." +
                capitalise(commandPath[1]) + "Controller";
        try {
            System.out.println("commandClassName: " + commandClassName);
            result = Class.forName(commandClassName);
        } catch (ClassNotFoundException e) {
            result = UnknownController.class;
            System.out.println("getCommandClass():" + e.getMessage());
        }
        return result;
    }

    private String capitalise(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
