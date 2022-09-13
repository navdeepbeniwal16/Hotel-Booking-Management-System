package lans.hotels.api;

import lans.hotels.controllers.UnknownCommand;
import lans.hotels.datasource.PostgresFacade;
import lans.hotels.datasource.connections.DBConnection;
import lans.hotels.domain.IDataSource;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "APIFront", value = "/api/*")
public class APIFront extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            DBConnection dbConnection = (DBConnection) getServletContext().getAttribute("DBConnection");
            IDataSource dataSourceLayer = PostgresFacade.newInstance(request.getSession(), dbConnection.connection());
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
            return (IFrontCommand) getCommandClass(request).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

    private Class getCommandClass(HttpServletRequest request) {
        Class result;
        final String commandClassName = "lans.hotels.controllers" +
                request.getParameter("command") + "Command";
        try {
            result = Class.forName(commandClassName);
        } catch (ClassNotFoundException e) {
            result = UnknownCommand.class;
        }
        return result;
    }
}
