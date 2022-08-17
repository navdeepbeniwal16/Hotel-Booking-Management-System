package lans.hotels.application.api;

import lans.hotels.datasource.DBConnection;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "ApiTest", value = "/api/test")
public class ApiTest extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DBConnection dbConnection = (DBConnection) getServletContext().getAttribute("DBConnection");

        Integer highestNumber = 0;
        try {
            Connection conn = dbConnection.connection();
            Statement stmt = conn.createStatement();
            String stmtStr = "SELECT MAX(column1) FROM test_table";
            ResultSet rs = stmt.executeQuery(stmtStr);
            if (rs.next()) {
                highestNumber = rs.getInt(1);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        // Hello
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("number", highestNumber);
        out.println(jsonObject);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}