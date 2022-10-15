package lans.hotels.api.controllers;


import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.HotelGroupSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.domain.hotel.Hotel;

import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.District;
import lans.hotels.use_cases.CreateHotelGroup;
import lans.hotels.use_cases.GetHotelGroups;
import lans.hotels.use_cases.GetSpecificHotelGroup;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.InvalidObjectException;


import java.sql.SQLException;
import java.util.ArrayList;


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
        else
            responseHelper.error("POST /hotel_groups must include 'search' or 'hotel_group'", HttpServletResponse.SC_BAD_REQUEST);
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
        else
            responseHelper.error("POST /hotel_groups search must include 'id'", HttpServletResponse.SC_BAD_REQUEST);
    }

    private void handleHotelGroupQuery() throws Exception {
        HotelGroup hg = getHotelGroupFromJsonObject(requestHelper.body());

        if (hg == null)
            return;

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

        JSONObject nestedJsonObject = jsonObject.getJSONObject("hotel_group");

        if(nestedJsonObject.has("name"))
        {
            hg_name = nestedJsonObject.getString("name");

            ArrayList<HotelGroup> hotel_groups = null;
            HotelGroupSearchCriteria hg_criteria = new HotelGroupSearchCriteria();
            hg_criteria.setHotelGroupName(hg_name);
            try {
                hotel_groups = dataSource.findBySearchCriteria(HotelGroup.class, hg_criteria);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (hotel_groups.size()>0) {
                responseHelper.error("POST /hotels hotel name is a duplicate", HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        }
        else{
            responseHelper.error("POST /hotelgroups hotel_group has incorrect request body 1", HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        if(nestedJsonObject.has("phone"))
            phone = nestedJsonObject.getString("phone");
        else {
            responseHelper.error("POST /hotelgroups hotel_group has incorrect request body 2", HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        if(nestedJsonObject.has("address"))
        {
            JSONObject addressJson = nestedJsonObject.getJSONObject("address");

            if(addressJson.has("line_1"))
                l1 = addressJson.getString("line_1");
            else{
                responseHelper.error("POST /hotelgroups hotel_group has incorrect request body 3", HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
            if(addressJson.has("line_2"))
                l2 = addressJson.getString("line_2");
            else{
                responseHelper.error("POST /hotelgroups hotel_group has incorrect request body 4", HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
            if(addressJson.has("city"))
                city = addressJson.getString("city");
            else{
                responseHelper.error("POST /hotelgroups hotel_group has incorrect request body 5", HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
            if(addressJson.has("postcode"))
                postcode = addressJson.getInt("postcode");
            else{
                responseHelper.error("POST /hotelgroups hotel_group has incorrect request body 6", HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
            if(addressJson.has("district"))
                district = addressJson.getString("district");
            else{
                responseHelper.error("POST /hotelgroups hotel_group has incorrect request body 7", HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        }
        else{
            responseHelper.error("POST /hotelgroups hotel_group has incorrect request body 8", HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
//        hg_name = (String) requestHelper.body("hotel_group", "name");
//        phone = (String) requestHelper.body("hotel_group", "phone");
//
//        if(addressJson.has("line_1")) l1 = addressJson.getString("line_1");
//        if(addressJson.has("line_2")) l2 = addressJson.getString("line_2");
//        if(addressJson.has("city")) city = addressJson.getString("city");
//        if(addressJson.has("postcode")) postcode = addressJson.getInt("postcode");
//        if(addressJson.has("district")) district = addressJson.getString("district");

        District district_ob = new District(district);
        Address address = new Address(l1, l2, district_ob, city, postcode);

        try {
            hg = new HotelGroup(dataSource,hg_name,address,phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hg;
    }
}