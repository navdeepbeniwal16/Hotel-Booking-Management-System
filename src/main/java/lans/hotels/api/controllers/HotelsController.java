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
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

public class HotelsController extends FrontCommand {
    @Override
    protected void concreteProcess() throws IOException, SQLException {
        System.out.println("HotelsController.concreteProcess(): " + request.getMethod() + " " + request.getRequestURI());
        switch (request.getMethod()) {
            case HttpMethod.GET:
                asAdmin(this::handleGet);
                return;
            case HttpMethod.POST:
                asCustomerOrHotelier(this::handlePost);
                return;
            case HttpMethod.PUT:
                asAdmin(this::handlePut);
                return;
            case HttpMethod.DELETE:
            default:
                responder.error("invalid request", HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public Void handleGet() throws Exception {

        useCase = new GetAllHotels(dataSource);
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responder.respond(useCase.getResult(), statusCode);
        return null;
    }

    public Void handlePut() throws Exception {
        if(requestBody.has("hotel")) {
            JSONObject JSONBody = requestBody.getJSONObject("hotel");
            if (JSONBody.has("id")) {
                Integer hotel_id = JSONBody.getInt("id");
                if(JSONBody.has("is_active")) {
                    Boolean is_active = JSONBody.getBoolean("is_active");
                    useCase = new ChangeHotelStatus(dataSource, hotel_id,is_active);
                    useCase.execute();
                    statusCode = useCase.succeeded() ?
                            HttpServletResponse.SC_OK :
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                    responder.respond(useCase.getResult(), statusCode);
            }
            else
                    responder.error("PUT /hotels does must include 'id' ", HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else
            responder.error("PUT /hotels does must include 'hotel' ", HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    public Void handlePost() throws Exception {
        if (requestBody.has("search")) {
            JSONObject searchQueryBody = requestBody.getJSONObject("search");
            handleSearchQuery(searchQueryBody);
        }
        else if (requestBody.has("hotel")) {
            handleHotelQuery(requestBody);
        } else {
            System.err.println("POST /api/hotels: " + Arrays.toString(commandPath));
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
            return null;
        }
        return null;
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
                responder.respond(useCase.getResult(), statusCode);
                return;
            }
        }
        else if(searchQueryBody.has("hotel_group_id")) {

            if(auth.isHotelier())
                if(!checkHotelGroupHotelierValid(searchQueryBody.getInt("hotel_group_id")))
                    return;

            useCase = new ViewHotelGroupHotels(dataSource,searchQueryBody.getInt("hotel_group_id"));
            useCase.execute();
            statusCode = useCase.succeeded() ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            responder.respond(useCase.getResult(), statusCode);
            return;
        }
        return;
    }

    private void handleHotelQuery(JSONObject body) throws IOException{
        Hotel h = getHotelFromJsonObject(body);

        if (h == null)
            throw new InvalidObjectException("Failed to parse hotel object from request body");

        if(auth.isHotelier())
            if(!checkHotelGroupHotelierValid(h.getHotelGroupID()))
                return;

        useCase = new CreateHotel(dataSource);
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responder.respond(useCase.getResult(), statusCode);
    }

    private boolean checkHotelGroupHotelierValid(Integer hg_id){
        Integer hotelier_hg_id = hotelierHotelGroupID();

        if(hotelier_hg_id != hg_id){
            responder.unauthorized();
            return false;
        }
        else return true;
    }

    public Integer hotelierHotelGroupID()
    {
        Integer hotelier_hg_id = -1;
        String hotelier_email = auth.getEmail();

        ArrayList<User> hoteliers = null;

        UserSearchCriteria u_criteria = new UserSearchCriteria();
        u_criteria.setEmail(hotelier_email);

        try {
            hoteliers = dataSource.findBySearchCriteria(User.class, u_criteria);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(hoteliers.size() > 0) {
            User hgh = hoteliers.get(0);
            hotelier_hg_id = hgh.getHotelierHotelGroupID();
        }
        return  hotelier_hg_id;
    }

    public Hotel getHotelFromJsonObject(JSONObject jsonObject) {

        Hotel h = null;
        int hotel_group_id = hotelierHotelGroupID();
        String h_name = "";
        String email ="";
        String l1 = "";
        String l2 = "";
        String city = "";
        int postcode = 0;
        String district = "";
        String contact = "";
        boolean is_active = true;

        if(jsonObject.has("hotelier_email")) {
            String hotelier_email = jsonObject.getString("hotelier_email");

            ArrayList<User> hoteliers = null;

            UserSearchCriteria u_criteria = new UserSearchCriteria();
            u_criteria.setEmail(hotelier_email);

            try {
                hoteliers = dataSource.findBySearchCriteria(User.class, u_criteria);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(hoteliers.size() > 0) {
                User hgh = hoteliers.get(0);

                hotel_group_id = hgh.getHotelierHotelGroupID();
            }
        }

        if(jsonObject.has("hotel")) {
            JSONObject nestedJsonObject = jsonObject.getJSONObject("hotel");

           if(nestedJsonObject.has("h_name"))
                h_name = nestedJsonObject.getString("h_name");
            if(nestedJsonObject.has("email"))
                email = nestedJsonObject.getString("email");
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

            District district_ob = new District(district);
            Address address = new Address(l1, l2, district_ob, city, postcode);

            try {
                h = new Hotel(dataSource,hotel_group_id,h_name,email,address,contact,
                        city,postcode,is_active);
            } catch (UoWException e) {
                e.printStackTrace();
            }
        }
        return h;
    }
}
