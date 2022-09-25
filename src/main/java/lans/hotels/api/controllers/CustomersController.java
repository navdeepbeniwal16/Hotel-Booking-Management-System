package lans.hotels.api.controllers;

import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.datasource.search_criteria.CustomerSearchCriteria;
import lans.hotels.domain.user_types.Customer;

import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.District;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomersController extends FrontCommand {
    @Override
    protected void concreteProcess() throws IOException {
        String[] commandPath = request.getPathInfo().split("/");
        if (commandPath.length != 2) {
            System.err.println("Customers controller: " + Arrays.toString(commandPath));
            System.err.println("Customers controller: commandPath.length = " + commandPath.length);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getRequestURI());
            return;

        } else {
            ArrayList<Customer> customers;
            switch (request.getMethod()) {
                case HttpMethod.GET:
                    try {
                        customers = dataSource.findAll(Customer.class);
                    } catch (Exception e) {
                        System.err.println("GET /api/customers: " + Arrays.toString(commandPath));
                        System.err.println("GET /api/customers: " + e.getMessage());
                        System.err.println("GET /api/customers: " + e.getClass());
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                        return;
                    }
                    returnCustomerJSON(customers);
                    return;
                case HttpMethod.POST:
                    JSONObject body = getRequestBody(request);
                    if (body.has("search")) {
                        CustomerSearchCriteria criteria = new CustomerSearchCriteria();
                        JSONObject searchQueryBody = body.getJSONObject("search");

                        if(searchQueryBody.has("id")) {
                            Integer customerID = searchQueryBody.getInt("id");
                            if(customerID != null) criteria.setCustomerId(customerID);
                        }
                        if (searchQueryBody.has("name")) {
                            String cName = searchQueryBody.getString("name");
                            if (cName != null) criteria.setName(cName);
                        }
                        if (searchQueryBody.has("email")) {
                            String email = searchQueryBody.getString("email");
                            if (email != null) criteria.setEmail(email);
                        }
                        if (searchQueryBody.has("is_active")) {
                            Boolean is_active = searchQueryBody.getBoolean("is_active");
                            if (is_active != null) criteria.setIsActive(is_active);
                        }


                        try {
                            customers = dataSource.findBySearchCriteria(Customer.class, criteria);
                        } catch (Exception e) {
                            System.err.println("POST /api/customers group: " + Arrays.toString(commandPath));
                            System.err.println("POST /api/customers group: " + e.getMessage());
                            System.err.println("POST /api/customers group: " + e.getClass());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                            return;
                        }

                        returnCustomerJSON(customers);
                        return;
                    }
                    else if (body.has("customer")) {

                        Customer customer = null;

                        customer = getCustomerFromJsonObject(body);

                        if(customer == null)
                            throw new InvalidObjectException("Failed to parse customer object from request body");

                        System.out.println("Parsed Customer Object : " + customer);

                        boolean success;
                        try{
                            success = dataSource.insert(Customer.class,customer);
                        } catch (Exception e) {
                            System.err.println("POST /api/customers: " + Arrays.toString(commandPath));
                            System.err.println("POST /api/customers: " + e.getMessage());
                            System.err.println("POST /api/customers: " + e.getClass());
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                            return;
                        }
                        try {
                            dataSource.commit();
                        } catch (DataSourceLayerException e) {
                            e.printStackTrace();
                        }

                        PrintWriter out = response.getWriter();
                        response.setStatus(200);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JSONObject aHG;
                        aHG = new JSONObject();
                        if(success)
                            aHG.put("created", success);
                        else
                            aHG.put("created",success);

                        JSONObject hgJSON = new JSONObject();
                        hgJSON.put("result", aHG);
                        out.print(hgJSON);
                        out.flush();
                        return;


                    }
                    else
                    {
                        System.err.println("POST /api/hoteliers: " + Arrays.toString(commandPath));
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                        return;
                    }

                case HttpMethod.PUT:

                case HttpMethod.DELETE:
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
            }
        }
    }

    public void returnCustomerJSON(ArrayList<Customer> customers) throws IOException {
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
            aCustomer.put("is_active", customer.getStatus());

            customerArray.put(aCustomer);
        }
        JSONObject customerJson = new JSONObject();
        customerJson.put("result", customerArray);
        out.print(customerJson);
        out.flush();
        return;
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

    public Customer getCustomerFromJsonObject(JSONObject jsonObject) {

        Customer customer = null;
        String name = "";
        String email = "";
        String password = "";
        String l1 = "";
        String l2 = "";
        String city = "";
        int postcode = 0;
        String district = "";
        String contact = "";
        int age=0;
        if(jsonObject.has("customer")) {
            JSONObject nestedJsonObject = jsonObject.getJSONObject("customer");

            if(nestedJsonObject.has("name"))
                name = nestedJsonObject.getString("name");
            if(nestedJsonObject.has("email"))
                email = nestedJsonObject.getString("email");
            if(nestedJsonObject.has("password"))
                password = nestedJsonObject.getString("password");
            if(nestedJsonObject.has("l1"))
                l1 = nestedJsonObject.getString("l1");
            if(nestedJsonObject.has("l2"))
                l2 = nestedJsonObject.getString("l2");
            if(nestedJsonObject.has("city"))
                city = nestedJsonObject.getString("city");
            if(nestedJsonObject.has("postcode"))
                postcode = nestedJsonObject.getInt("postcode");
            if(nestedJsonObject.has("district"))
                district = nestedJsonObject.getString("district");
            if(nestedJsonObject.has("contact"))
                contact = nestedJsonObject.getString("contact");
            if(nestedJsonObject.has("age"))
                age = nestedJsonObject.getInt("age");

            District district_ob = new District(district);
            Address address = new Address(l1, l2, district_ob, city, postcode);

            customer = new Customer(dataSource,name,email,password,3,address,contact,age,true);
        }
        return customer;
    }
}

