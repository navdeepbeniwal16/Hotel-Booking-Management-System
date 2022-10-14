package lans.hotels.api.controllers;

import lans.hotels.datasource.exceptions.UoWException;
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
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                    responseHelper.respond(useCase.getResult(), statusCode);
            }
            else
                    responseHelper.error("PUT /hotels does must include 'id' ", HttpServletResponse.SC_BAD_REQUEST);
            }
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
            System.err.println(requestHelper.methodAndURI());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
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
        }
        else if(searchQueryBody.has("hotels")) {
            if(!auth.isHotelier())
            {
                responseHelper.unauthorized();
                return;
            }
            Integer hotel_group_id = auth.getUser().getHotelierHotelGroupID();
            System.out.println("user hotel group id : "+auth.getUser().getHotelierHotelGroupID());

            useCase = new ViewHotelGroupHotels(dataSource,hotel_group_id);
            useCase.execute();
            statusCode = useCase.succeeded() ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            responseHelper.respond(useCase.getResult(), statusCode);
            return;
        }
        return;
    }

    private void handleHotelQuery(JSONObject body) throws IOException{
        Hotel h = getHotelFromJsonObject(body);

        if (h == null)
            throw new InvalidObjectException("Failed to parse hotel object from request body");

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
