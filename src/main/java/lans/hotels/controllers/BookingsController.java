package lans.hotels.controllers;

import lans.hotels.api.HttpMethod;
import lans.hotels.api.exceptions.CommandException;
import lans.hotels.datasource.search_criteria.CustomerSearchCriteria;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.user_types.Customer;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BookingsController extends FrontCommand {
    @Override
    protected void concreteProcess() throws CommandException, IOException, SQLException {
        String[] commandPath = request.getPathInfo().split("/");
        switch (request.getMethod()) {
            case HttpMethod.GET:
                // TODO: TBR - Just for testing
                System.out.println("Request transferred to Booking Controller GET Method");
                CustomerSearchCriteria criteria = new CustomerSearchCriteria();
                criteria.setCustomerId(1);
                try {
                    ArrayList<Customer> customers = dataSource.findBySearchCriteria(Customer.class, criteria);
                    if(!customers.isEmpty()) {
                        Customer customer = customers.get(0);
                        System.out.println("Number of bookings found:" + customer.getAllBookings().size());
                    }
                    System.out.println(customers.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(commandPath.length == 2) {
                     // GET: /api/bookings
                    JSONObject requestBody = getRequestBody(request);
                    if(requestBody.has("search")) {
                        JSONObject searchQuery = requestBody.getJSONObject("search");
                        CustomerSearchCriteria customerSearchCriteria = new CustomerSearchCriteria();
                        if(searchQuery.has("customerId")) {
                            Integer customerId = searchQuery.getInt("customerId");
                            if(customerId != null) customerSearchCriteria.setCustomerId(customerId);
                            Customer customer;
                            try {
                                // Fetching the customer based on the customer id
                                ArrayList<Customer> customers = dataSource.findBySearchCriteria(Customer.class, criteria);
                                if(!customers.isEmpty()) {
                                    customer = customers.get(0);

                                    // Making call to customer to fetch all of it's booking details
                                    ArrayList<Booking> customerBookings = customer.getAllBookings();


                                    for(Booking booking: customerBookings) {
                                        System.out.println("Booking : " + booking.toString());
                                    }
                                    JSONObject customersBookingsJson = getBookingsJson(customerBookings);
                                    sendResponse(customersBookingsJson);
                                } else {
                                    System.out.println("Not a customer bookings query!!!");
                                }

                            } catch (Exception e) {
                                System.err.println("GET /api/bookings: " + Arrays.toString(commandPath));
                                System.err.println("GET /api/bookings: " + e.getMessage());
                                System.err.println("GET /api/bookings: " + e.getClass());
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                                return;
                            }



                        }
                    }

                }
                break;
            case HttpMethod.POST:
                break;
            case HttpMethod.PUT:
                break;
            case HttpMethod.DELETE:
                break;
        }
    }

    public JSONObject getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader requestReader = request.getReader();

        String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println("Request Body Lines + " + lines);
        JSONObject body;
        if (lines.length() > 0) {
            System.out.println(lines);
            body = new JSONObject(lines);
        } else {
            return null;
        }
        return body;
    }

    public JSONObject getBookingsJson(ArrayList<Booking> bookings) {
        JSONObject jsonBookingsObject = new JSONObject();
        JSONArray nestedBookingsArray = new JSONArray();

        JSONObject abooking;
        for (Booking booking: bookings) {
            abooking = new JSONObject();
            abooking.put("hotelId", booking.getHotelId());
            abooking.put("customerId", booking.getCustomerId());
//            abooking.put("startDate", booking.getDateRange().getTo().toString());
//            abooking.put("endDate", booking.getDateRange().getFrom().toString());
            nestedBookingsArray.put(abooking);
        }

        jsonBookingsObject.put("result", nestedBookingsArray);
        return jsonBookingsObject;
    }

    public void sendResponse(JSONObject jsonObject) {
        try {
            PrintWriter out = response.getWriter();
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(jsonObject);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
