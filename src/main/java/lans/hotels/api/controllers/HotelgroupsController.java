package lans.hotels.api.controllers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.District;
import lans.hotels.use_cases.CreateHotelGroup;
import lans.hotels.use_cases.GetAllHotelGroupDetails;
import lans.hotels.use_cases.GetSpecificHotelGroup;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.sql.SQLException;

public class HotelgroupsController extends FrontCommand {
    @Override
    protected void concreteProcess() throws IOException, SQLException {
        System.out.println("HotelGroupsController.concreteProcess(): " + request.getMethod() + " " + request.getRequestURI());
        switch (request.getMethod()) {
            case HttpMethod.GET:
                asAdmin(this::handleGet);
                return;
            case HttpMethod.POST:
                asAdmin(this::handlePost);
                return;
            case HttpMethod.PUT:
            case HttpMethod.DELETE:
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        }
    }

    public Void handleGet() throws Exception {
        useCase = new GetAllHotelGroupDetails(dataSource);
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responseHelper.respond(useCase.getResult(), statusCode);
        return null;
    }

    public Void handlePost() throws Exception {
        if (requestHelper.body().has("search"))
            handleSearchQuery();
        else if (requestHelper.body().has("hotel_group"))
            handleHotelGroupQuery();
        else {
            responseHelper.error("POST /hotel_groups must include 'search' or 'hotel_group'", HttpServletResponse.SC_BAD_REQUEST);
        }
        return null;
    }

    private void handleSearchQuery() {
        JSONObject searchQueryBody = requestHelper.body().getJSONObject("search");
        if (searchQueryBody.has("id")) {
            Integer hotelGroupId = searchQueryBody.getInt("id");

            useCase = new GetSpecificHotelGroup(dataSource, hotelGroupId);
            useCase.execute();
            statusCode = useCase.succeeded() ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            responseHelper.respond(useCase.getResult(), statusCode);
        }
    }

    private void handleHotelGroupQuery() throws Exception {
        HotelGroup hg = getHotelGroupFromJsonObject(requestHelper.body());

        if (hg == null)
            throw new InvalidObjectException("Failed to parse hotel group object from request body");

        useCase = new CreateHotelGroup(dataSource);
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responseHelper.respond(useCase.getResult(), statusCode);
    }

    public HotelGroup getHotelGroupFromJsonObject(JSONObject jsonObject) {

        HotelGroup hg = null;
        String hg_name = "";
        String l1 = "";
        String l2 = "";
        String city = "";
        int postcode = 0;
        String district = "";
        String phone = "";

        if(jsonObject.has("hotel_group")) {
            JSONObject nestedJsonObject = jsonObject.getJSONObject("hotel_group");

            if(nestedJsonObject.has("hg_name"))
                hg_name = nestedJsonObject.getString("hg_name");
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
            if(nestedJsonObject.has("phone"))
                phone = nestedJsonObject.getString("phone");

            District district_ob = new District(district);
            Address address = new Address(l1, l2, district_ob, city, postcode);

            try {
                hg = new HotelGroup(dataSource,hg_name,address,phone);
            } catch (UoWException e) {
                e.printStackTrace();
            }
        }
        return hg;
    }
}