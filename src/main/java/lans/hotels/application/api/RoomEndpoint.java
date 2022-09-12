package lans.hotels.application.api;

import lans.hotels.datasource.DBConnection;
import lans.hotels.datasource.UnitOfWork;
import lans.hotels.datasource.mappers.RoomMapper;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.hotel.HotelBuilder;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.room.RoomBuilder;
import lans.hotels.domain.room.RoomSpecification;
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
import java.util.Map;

@WebServlet(name = "RoomEndpoint", value = "/api/room/*")
public class RoomEndpoint extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UnitOfWork.handleSession(request.getSession());
        DBConnection dbConnection = (DBConnection) getServletContext().getAttribute("DBConnection");
        // TODO: error handling - id is not an int
        System.out.println("GET /api/room/" + request.getPathInfo().substring(1));
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
            jsonObject.put("id", room.getUid());
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
        // TODO: error handling - id is not an int
        System.out.println("POST /api/room/" + request.getPathInfo().substring(1));
        Integer hotelId;
        try {
            hotelId = Integer.parseInt(request.getPathInfo().substring(1));
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);

            return;
        }

        HotelBuilder hotelBuilder = new HotelBuilder();
        Hotel hotel = hotelBuilder.uid(hotelId).getResult();

        // TODO: error handling - hotel does not exist
        // TODO: implement HotelMapper

        // TODO: add defensive programming against bad requests

        Map params = request.getParameterMap();

        RoomSpecification specification;
        if (params.containsKey(RoomParams.specId)) {
            specification = new RoomSpecification(Integer.parseInt(request.getParameter(RoomParams.specId)));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            throw new ServletException("Error: POST /api/room/:hotel - room must include room specification ID");
        }

        RoomBuilder roomBuilder = new RoomBuilder(hotel, specification);

        if (params.containsKey(RoomParams.number)) {
            Integer number = Integer.parseInt(request.getParameter(RoomParams.number));
            roomBuilder.number(number);
        }

        if (params.containsKey(RoomParams.floor)) {
            Integer floor = Integer.parseInt(request.getParameter(RoomParams.floor));
            roomBuilder.number(floor);
        }

        Room room = roomBuilder.getResult();
        try {
            DBConnection dbConnection = (DBConnection) getServletContext().getAttribute("DBConnection");
            Connection conn = dbConnection.connection();
            RoomMapper mapper = new RoomMapper(conn);
            room = mapper.create(room);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        // TODO: extract duplicate code between doPost and doGet.
        if (room != null) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", room.getUid());
            jsonObject.put("floor_number", room.getRoomFloor());
            jsonObject.put("room_number", room.getRoomNumber());
            out.println(jsonObject);
            out.flush();
        } else {
            System.out.println("Error: POST /api/room/:hotel - create new room failed");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    private class RoomParams {
        protected static final String specId = "spec_id";
        protected static final String number = "number";
        protected static final String floor = "floor";
    }
}
