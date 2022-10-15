package lans.hotels.api.controllers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.datasource.search_criteria.UserSearchCriteria;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.user.User;
import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.District;
import lans.hotels.use_cases.*;
import lans.hotels.use_cases.CreateHotel;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.util.*;

public class HotelsController extends FrontCommand {
    @Override
    protected void concreteProcess() {
        System.out.println("HotelsController.concreteProcess(): " + request.getMethod() + " " + request.getRequestURI());
        switch (request.getMethod()) {
            case HttpMethod.GET:
                handleGet();
                return;
            case HttpMethod.POST:
                try {
                    handlePost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            case HttpMethod.PUT:
                asAdmin(this::handlePut);
                return;
            default:
                responseHelper.unimplemented(request.getMethod() + " /users");
        }
    }

    public void handleGet() {

        useCase = new GetAllHotels(dataSource);
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responseHelper.respond(useCase.getResult(), statusCode);
    }

    public Void handlePut() throws Exception {
        if(requestHelper.body().has("hotel")) {
            JSONObject JSONBody = requestHelper.body().getJSONObject("hotel");
            if (JSONBody.has("id")) {
                Integer hotel_id = JSONBody.getInt("id");
                if(JSONBody.has("is_active")) {
                    Boolean is_active = JSONBody.getBoolean("is_active");
                    useCase = new ChangeHotelStatus(dataSource, hotel_id,is_active);
                    useCase.execute();
                    statusCode = useCase.succeeded() ?
                            HttpServletResponse.SC_OK :
                            HttpServletResponse.SC_BAD_REQUEST;
                    responseHelper.respond(useCase.getResult(), statusCode);
                }
                else
                    responseHelper.error("PUT /hotels does must include 'is_active' ", HttpServletResponse.SC_BAD_REQUEST);
            }
            else
                responseHelper.error("PUT /hotels does must include 'id' ", HttpServletResponse.SC_BAD_REQUEST);
        }
        else
            responseHelper.error("PUT /hotels does must include 'hotel' ", HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    private void handlePost() throws Exception {
        if (requestHelper.body().has("search")) {
            JSONObject searchQueryBody = requestHelper.body().getJSONObject("search");
            handleSearchQuery(searchQueryBody);
        }
        else if (requestHelper.body().has("hotel")) {
            handleHotelQuery(requestHelper.body());
        } else {
            responseHelper.error("POST /hotels does must include 'search' or 'hotel' ", HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void handleSearchQuery(JSONObject searchQueryBody) throws Exception {
        if (searchQueryBody.has("city")) {
            String city = searchQueryBody.getString("city");
            if (city != null){
                useCase = new SearchHotelsByLocation(dataSource,city);
                useCase.execute();
                statusCode = useCase.succeeded() ?
                        HttpServletResponse.SC_OK :
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                responseHelper.respond(useCase.getResult(), statusCode);
                return;
            }
            else
                responseHelper.error("POST /hotels search needs to contain city name ", HttpServletResponse.SC_BAD_REQUEST);
        }
        else if(searchQueryBody.has("hotels")) {
            if(!auth.isHotelier())
            {
                responseHelper.unauthorized();
                return;
            }

            Integer hotel_group_id = auth.getUser().getHotelierHotelGroupID();
            if(hotel_group_id==0)
            {
                responseHelper.error("POST /hotels hotelier is not assigned to any hotel group ", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            System.out.println("user hotel group id : "+auth.getUser().getHotelierHotelGroupID());

            useCase = new ViewHotelGroupHotels(dataSource,hotel_group_id);
            useCase.execute();
            statusCode = useCase.succeeded() ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            responseHelper.respond(useCase.getResult(), statusCode);
            return;
        }
        else
            responseHelper.error("POST /hotels search has incorrect request body ", HttpServletResponse.SC_BAD_REQUEST);

        return;
    }

    private void handleHotelQuery(JSONObject body) throws IOException{
        Hotel h = getHotelFromJsonObject(body);

        if (h == null)
            return;

        useCase = new CreateHotel(dataSource);
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responseHelper.respond(useCase.getResult(), statusCode);
    }

    public Hotel getHotelFromJsonObject(JSONObject jsonObject) {

        Hotel h = null;
        int hotel_group_id = auth.getUser().getHotelierHotelGroupID();
        String h_name = "";
        String email ="";
        String l1 = "";
        String l2 = "";
        String city = "";
        int postcode = 0;
        String district = "";
        String contact = "";
        boolean is_active = true;

        JSONObject nestedJsonObject = jsonObject.getJSONObject("hotel");

        if(nestedJsonObject.has("h_name")) {
            h_name = nestedJsonObject.getString("h_name");

            ArrayList<Hotel> hotels = null;
            HotelSearchCriteria h_criteria = new HotelSearchCriteria();
            h_criteria.setName(h_name);
            try {
                hotels = dataSource.findBySearchCriteria(Hotel.class, h_criteria);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (hotels.size()>0){
                responseHelper.error("POST /hotels hotel name is a duplicate", HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        }
        else{
            responseHelper.error("POST /hotels hotel has incorrect request body ", HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        if(nestedJsonObject.has("email")) {
            email = nestedJsonObject.getString("email");

            ArrayList<Hotel> hotels = null;
            HotelSearchCriteria h_criteria = new HotelSearchCriteria();
            h_criteria.setEmail(email);
            try {
                hotels = dataSource.findBySearchCriteria(Hotel.class, h_criteria);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (hotels.size()>0){
                responseHelper.error("POST /hotels hotel email is a duplicate", HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        }
        else{
            responseHelper.error("POST /hotels hotel has incorrect request body ", HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        if(nestedJsonObject.has("l1"))
            l1 = nestedJsonObject.getString("l1");
        else{
            responseHelper.error("POST /hotels hotel has incorrect request body ", HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        if(nestedJsonObject.has("l2"))
            l2 = nestedJsonObject.getString("l2");
        else{
            responseHelper.error("POST /hotels hotel has incorrect request body ", HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        if(nestedJsonObject.has("city"))
            city = nestedJsonObject.getString("city");
        else{
            responseHelper.error("POST /hotels hotel has incorrect request body ", HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        if(nestedJsonObject.has("postcode"))
            postcode = nestedJsonObject.getInt("postcode");
        else{
            responseHelper.error("POST /hotels hotel has incorrect request body ", HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        if(nestedJsonObject.has("district"))
            district = nestedJsonObject.getString("district");
        else{
            responseHelper.error("POST /hotels hotel has incorrect request body ", HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        if(nestedJsonObject.has("contact"))
            contact = nestedJsonObject.getString("contact");
        else{
            responseHelper.error("POST /hotels hotel has incorrect request body ", HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        District district_ob = new District(district);
        Address address = new Address(l1, l2, district_ob, city, postcode);

        try {
            h = new Hotel(dataSource,hotel_group_id,h_name,email,address,contact,
                    city,postcode,is_active);
        } catch (UoWException e) {
            e.printStackTrace();
        }
        return h;
    }
}
