package lans.hotels.api;

import lans.hotels.api.APIFrontController;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class APIFrontControllerTest {
    private final String mockHotelName = "Mock Hotel";
    private final String mockHotelEmail = "hotel@mock.com";
    private final String mockHotelAddress = "123 Mock Street";
    private final Integer mockHotelCountry = 1;
    private final Integer mockHotelArea = 2;
    private final Integer mockHotelNumber = 3456;
    private JSONObject mockHotelJson;

    @Spy private APIFrontController servlet;
    @Mock private ServletConfig config;
    @Mock private ServletContext context;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private PreparedStatement prepareStatement;
    @Mock private ResultSet resultSet;
    @Mock private ByteArrayOutputStream outputStream;
    @Mock private Connection connection;

    @BeforeEach
    void beforeEach() {
        JSONObject mockHotelJson = new JSONObject();
        mockHotelJson.put("name", mockHotelName);
        mockHotelJson.put("email", mockHotelEmail);
        mockHotelJson.put("address", mockHotelAddress);
        mockHotelJson.put("country", mockHotelCountry);
        mockHotelJson.put("area", mockHotelArea);
        mockHotelJson.put("number", mockHotelNumber);
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    void xGET_hotel_by_id() throws IOException, ServletException, SQLException {
//        config = mock(ServletConfig.class);
//        context = mock(ServletContext.class);
//        when(context.getContext("/api/*")).thenReturn(context);
//        request = mock(HttpServletRequest.class);
//        response = mock(HttpServletResponse.class);
//        prepareStatement = mock(PreparedStatement.class);
//        resultSet = mock(ResultSet.class);
//
//        when(request.getPathInfo()).thenReturn("/api/hotels/" + 1);
//        when(response.getWriter()).thenReturn(new PrintWriter(outputStream));
//        when(prepareStatement.executeQuery()).thenReturn(resultSet);
//        when(resultSet.next()).thenReturn(true).thenReturn(false);
//        when(resultSet.getString("name")).thenReturn(mockHotelName);
//        when(resultSet.getString("email")).thenReturn(mockHotelEmail);
//        when(resultSet.getString("line_1")).thenReturn(mockHotelAddress);
//        when(resultSet.getInt("country")).thenReturn(mockHotelCountry);
//        when(resultSet.getInt("area")).thenReturn(mockHotelArea);
//        when(resultSet.getInt("number")).thenReturn(mockHotelNumber);
//
//
//        servlet = new APIFrontController();
//        servlet.init(config);
//        servlet.doGet(request, response);
//
//
//        JSONObject actual = new JSONObject(outputStream.toString());
//        Assertions.assertEquals(mockHotelJson, actual);
    }
}