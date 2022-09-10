package lans.hotels.application.api;

import lans.hotels.datasource.DBConnection;
import lans.hotels.datasource.mappers.RoomMapper;
import lans.hotels.domain.hotel.Room;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "RoomEndpoint", value = "/api/room/*")
public class RoomEndpoint extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DBConnection dbConnection = (DBConnection) getServletContext().getAttribute("DBConnection");
        // TODO: error handling - id is not an int
        System.out.println("endpoint: " + request.getPathInfo().substring(1));
        Integer id;
        try {
            id = Integer.parseInt(request.getPathInfo().substring(1));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Room room = null;
        try {
            Connection conn = dbConnection.connection();
            RoomMapper mapper = new RoomMapper(conn);
            room = mapper.getById(id);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        if (room != null) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", room.getId());
            jsonObject.put("floor_number", room.getRoomFloor());
            jsonObject.put("room_number", room.getRoomNumber());
            out.println(jsonObject);
            out.flush();
        } else {
            System.out.println("Room " + id + " not found");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
