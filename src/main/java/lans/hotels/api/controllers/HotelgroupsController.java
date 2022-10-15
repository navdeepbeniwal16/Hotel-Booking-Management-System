package lans.hotels.api.controllers;

import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.District;
import lans.hotels.use_cases.CreateHotelGroup;
import lans.hotels.use_cases.GetHotelGroups;
import lans.hotels.use_cases.GetSpecificHotelGroup;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.InvalidObjectException;

public class HotelgroupsController extends FrontCommand {
    @Override
    protected void concreteProcess() {
        System.out.println("HotelGroupsController.concreteProcess(): " + request.getMethod() + " " + request.getRequestURI());
        switch (request.getMethod()) {
            case HttpMethod.GET:
                asHotelierOrAdmin(this::handleGet);
                return;
            case HttpMethod.POST:
                asAdmin(this::handlePost);
                return;
            default:
                responseHelper.unimplemented(request.getMethod() + " /hotelgroups");
        }
    }

    public Void handleGet() {
        Integer groupId = -1;
        if (auth.isHotelier()) {
            groupId = auth.hotelGroupId();
        }
        useCase = new GetHotelGroups(dataSource, groupId);
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

        JSONObject details = requestHelper.body("hotel_group");
        if(!details.isEmpty()) {
            hg_name = (String) requestHelper.body("hotel_group", "name");
            phone = (String) requestHelper.body("hotel_group", "phone");
            JSONObject addressJson = (JSONObject) requestHelper.body("hotel_group", "address");
            if(addressJson.has("line_1")) l1 = addressJson.getString("line_1");
            if(addressJson.has("line_2")) l2 = addressJson.getString("line_2");
            if(addressJson.has("city")) city = addressJson.getString("city");
            if(addressJson.has("postcode")) postcode = addressJson.getInt("postcode");
            if(addressJson.has("district")) district = addressJson.getString("district");


            District district_ob = new District(district);
            Address address = new Address(l1, l2, district_ob, city, postcode);

            try {
                hg = new HotelGroup(dataSource,hg_name,address,phone);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return hg;
    }
}