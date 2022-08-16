package lans.hotels.application.api;

import lans.hotels.datasource.DBConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
@WebServlet(name = "helloServlet", value = "")
public class HelloServlet extends HttpServlet {
    private String hello;

    public HelloServlet() {
        super();
        System.out.println("HelloServlet constructor()");
    }
    public void init() {
        hello = "Hello, LANS Hotels!";
        System.out.println("HelloServlet.init()");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DBConnection dbConnection = (DBConnection) getServletContext().getAttribute("DBConnection");

        String message = "No items in database";
        try {
            Connection conn = dbConnection.connection();
            Statement stmt = conn.createStatement();
            String stmtStr = "SELECT MAX(column1) FROM test_table";
            ResultSet rs = stmt.executeQuery(stmtStr);
            if (rs.next()) {
                Integer column1Row1 = rs.getInt(1);
                message = "Highest number in the database: " + column1Row1;
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + hello + "</h1>");
        out.println("<p>" + message + "</p>");
        out.println("</body></html>");


    }

    public void destroy() {
    }
}