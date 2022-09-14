package lans.hotels.api;

import lans.hotels.controllers.UnknownCommand;
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
            throw new ServletException(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private IFrontCommand getCommand(HttpServletRequest request) throws ServletException {
        try {
            String[] commandPath = request.getPathInfo().split("/");
            return (IFrontCommand) getCommandClass(commandPath).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

    private Class getCommandClass(String[] commandPath) {
        if (commandPath.length == 0) return UnknownCommand.class;
        Class result;
        final String commandClassName = "lans.hotels.controllers" +
                capitalise(commandPath[0]) + "Command";
        try {
            result = Class.forName(commandClassName);
        } catch (ClassNotFoundException e) {
            result = UnknownCommand.class;
        }
        return result;
    }

    private String capitalise(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
