package lans.hotels.api.controllers;

import lans.hotels.api.DTOs.AddressDTO;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.use_cases.*;
import lans.hotels.use_cases.CreateHotel;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class HotelsController extends FrontCommand {
    @Override
    protected void concreteProcess() {
        System.out.println("HotelsController.ConcreteProcess(): " + request.getMethod() + " " + request.getRequestURI());
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

    public Void handlePut() {
        Integer hotelId = (Integer) requestHelper.body("hotel", "id");
        Boolean isActive = (Boolean) requestHelper.body("hotel", "is_active");
        if (hotelId != null && isActive != null) {
            useCase = new ChangeHotelStatus(dataSource, hotelId,isActive);
            useCase.execute();
            statusCode = useCase.succeeded() ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_BAD_REQUEST;
            responseHelper.respond(useCase.getResult(), statusCode);
        } else {
            responseHelper.error("PUT /hotels | invalid request body: hotel_id=" + hotelId + ", is_active=" + isActive, HttpServletResponse.SC_BAD_REQUEST);
        }
        return null;
    }

    private void handlePost() throws Exception {
        if (requestHelper.body().has("search")) {
            JSONObject searchQueryBody = requestHelper.body().getJSONObject("search");
            handleSearchQuery(searchQueryBody);
        }
        else if (requestHelper.body().has("hotel")) {
            handleCreateHotel();
        } else {
            responseHelper.error("POST /hotels does must include 'search' or 'hotel' ", HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void handleSearchQuery(JSONObject searchQueryBody) {
        if (searchQueryBody.has("city")) {
            String city = searchQueryBody.getString("city");
            handleCitySearch(city);
        }
        else if(searchQueryBody.has("hotels")) {
            asHotelier(this::hotelsForHotelierGroup);
        }
        else
            responseHelper.error("POST /hotels | invalid incorrect request body: " + requestHelper.body(), HttpServletResponse.SC_BAD_REQUEST);
    }

    private void handleCitySearch(String city) {
        if (city != null){
            useCase = new SearchHotelsByLocation(dataSource,city);
            useCase.execute();
            statusCode = useCase.succeeded() ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            responseHelper.respond(useCase.getResult(), statusCode);
        }
        else
            responseHelper.error("POST /hotels search needs to contain city name ", HttpServletResponse.SC_BAD_REQUEST);
    }

    private Void hotelsForHotelierGroup() {
        Integer hotel_group_id = auth.getUser().getHotelierHotelGroupID();
        if((hotel_group_id == 0) || (hotel_group_id == -1))
        {
            responseHelper.error("POST /hotels hotelier is not assigned to any hotel group ", HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        useCase = new ViewHotelGroupHotels(dataSource,hotel_group_id);
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responseHelper.respond(useCase.getResult(), statusCode);
        return null;
    }

    private void handleCreateHotel() {
        try {
            Hotel hotel = getHotelFromJsonObject();
            if (hotel == null) {
                throw new Error("error with json: " + requestHelper.body());
            }
            useCase = new CreateHotel(dataSource);
            useCase.execute();
            statusCode = useCase.succeeded() ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            responseHelper.respond(useCase.getResult(), statusCode);
        } catch (Exception e) {
            responseHelper.error(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public Hotel getHotelFromJsonObject() throws Exception {

        Hotel h = null;
        String name;
        String email;
        String contact = (String) requestHelper.body("hotel", "phone");
        int hotel_group_id = auth.getUser().getHotelierHotelGroupID();
        boolean is_active = true;

        if (hasRequiredFields()){
            name = (String) requestHelper.body("hotel", "name");
            email = (String) requestHelper.body("hotel", "email");
        } else {
            throw new Exception("invalid request body: " + requestHelper.body());
        }

        if (isNewHotelDuplicate()) {
            throw new Exception("no duplicate hotels: " + name + " | " + email);
        }

        AddressDTO addressDTO = new AddressDTO((JSONObject) requestHelper.body("hotel", "address"));

        try {
            h = new Hotel(dataSource,
                    hotel_group_id,
                    (String) requestHelper.body("hotel", "name"),
                    email,
                    addressDTO.object(),
                    contact,
                    addressDTO.object().getCity(),
                    addressDTO.object().getPostCode(),
                    is_active);
        } catch (UoWException e) {
            e.printStackTrace();
        }
        return h;
    }

    private boolean hasRequiredFields() {
        return requestHelper.body().has("hotel") &&
                requestHelper.body("hotel").has("name") &&
                requestHelper.body("hotel").has("phone") &&
                requestHelper.body("hotel").has("email") &&
                requestHelper.body("hotel").has("address") &&
                ((JSONObject) requestHelper.body("hotel", "address")).has("line_1") &&
                ((JSONObject) requestHelper.body("hotel", "address")).has("district") &&
                ((JSONObject) requestHelper.body("hotel", "address")).has("city") &&
                ((JSONObject) requestHelper.body("hotel", "address")).has("postcode");
    }

    private boolean isNewHotelDuplicate() {
        if (!hasRequiredFields()) return false;

        try {
            HotelSearchCriteria nameCriteria = new HotelSearchCriteria();
            nameCriteria.setName(requestHelper.body("hotel").getString("name"));

            ArrayList<Hotel> hotels = dataSource.findBySearchCriteria(Hotel.class, nameCriteria);

            if (hotels.size() > 0) return true;

            HotelSearchCriteria emailCriteria = new HotelSearchCriteria();
            emailCriteria.setEmail(requestHelper.body("hotel").getString("email"));
            hotels = dataSource.findBySearchCriteria(Hotel.class, emailCriteria);

            if (hotels.size() > 0) return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
