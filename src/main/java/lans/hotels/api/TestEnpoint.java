package lans.hotels.api;

import lans.hotels.datasource.connections.DBConnection;
import lans.hotels.datasource.mappers.IMapperRegistry;
import lans.hotels.datasource.mappers.PostgresMapperRegistry;
import lans.hotels.domain.IDataSource;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "TestEnpoint", value = "/TestEnpoint")
public class TestEnpoint extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnection dbConnection = (DBConnection) getServletContext().getAttribute("DBConnection");
        IMapperRegistry mapperRegistry = PostgresMapperRegistry.getInstance(new HashMap<>(), dbConnection);
        IDataSource dataSource;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
