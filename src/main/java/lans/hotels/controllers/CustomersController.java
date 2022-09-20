package lans.hotels.controllers;

import lans.hotels.api.HttpMethod;
import lans.hotels.domain.user_types.Customer;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomersController extends FrontCommand {
    @Override
    protected void concreteProcess() throws IOException {
        switch(request.getMethod()) {
            case HttpMethod.GET:
                String[] commandPath = request.getPathInfo().split("/");


                if (commandPath.length==2) {
                    // GET /api/hotels
                    BufferedReader requestReader = request.getReader();

                    String lines = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
                    System.out.println(lines);
                    JSONObject body = new JSONObject();

                    if (lines.length() > 0) {
                        System.out.println(lines);
                         body = new JSONObject(lines);
                    }

                    if (body.has("search")) {
                        // perform search
                    } else {
                        ArrayList<Customer> customers;
                        try {
                            customers = dataSource.findAll(Customer.class);
                        } catch (Exception e) {
                            System.err.println("GET /api/customers: " + Arrays.toString(commandPath));
                            System.err.println("GET /api/customers: " + e.getMessage());
                            System.err.println("GET /api/customers: " + e.getClass());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                            return;
                        }

                        JSONArray customerArray = new JSONArray();
                        PrintWriter out = response.getWriter();
                        response.setStatus(200);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JSONObject aCustomer;
                        for (Customer customer: customers) {

                            aCustomer = new JSONObject();
                            aCustomer.put("customer_id", customer.getId());
                            aCustomer.put("name",customer.getName());
                            aCustomer.put("email",customer.getEmail());
                            aCustomer.put("address", customer.getAddress().toString());
                            aCustomer.put("contact", customer.getContact());
                            aCustomer.put("age", customer.getAge());

                            customerArray.put(aCustomer);
                        }
                        JSONObject customerJson = new JSONObject();
                        customerJson.put("result", customerArray);
                        out.print(customerJson);
                        out.flush();
                        return;
                    }
                } else {
                    System.err.println("Customers contoller: " + Arrays.toString(commandPath));
                    System.err.println("Customers contoller: commandPath.length = " + commandPath.length);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
                    return;
                 }

                return;
            case HttpMethod.POST:
                case HttpMethod.PUT:
            case HttpMethod.DELETE:
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        }
    }

    private void getCustomer(Integer id) throws IOException {

        Customer customer = (Customer) dataSource.find(Customer.class, id);

        if (customer == null) {
            System.out.println("404 NOT FOUND");
            System.out.println("\tCustomerController.getCustomer(): customer.id=" + id);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        } else {
            JSONObject json = new JSONObject();
            PrintWriter out = response.getWriter();
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            json.put("id", customer.getId());
            json.put("name", customer.getAddress().toString());
            json.put("email", customer.getContact());
            json.put("address", customer.getAge());
            out.print(json);
            out.flush();
        }
    }
}
