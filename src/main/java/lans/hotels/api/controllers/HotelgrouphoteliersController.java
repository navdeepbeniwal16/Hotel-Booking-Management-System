package lans.hotels.api.controllers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.HotelGroupHotelierSearchCriteria;
import lans.hotels.domain.hotel_group.HotelGroupHotelier;
import lans.hotels.use_cases.AddHotelierToHotelGroup;
import lans.hotels.use_cases.RemoveHotelierFromHotelGroup;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.util.*;

public class HotelgrouphoteliersController extends FrontCommand {
    @Override
    protected void concreteProcess() throws IOException, SQLException {
        System.out.println("HotelGroupHoteliers.concreteProcess(): " + request.getMethod() + " " + request.getRequestURI());

        switch(request.getMethod()) {
            case HttpMethod.GET:
            case HttpMethod.POST:
                asAdmin(this::handlePost);
                return;
            case HttpMethod.PUT:
                return;
            case HttpMethod.DELETE:
                asAdmin(this::handleDelete);
                return;
        }
    }

    public Void handlePost() throws Exception {
        if (requestHelper.body().has("hotel_group_hotelier")) {
            HotelGroupHotelier hgh = getHotelGroupHotelierFromJsonObject(requestHelper.body());

            if (hgh == null)
                throw new InvalidObjectException("Failed to parse hotel object from request body");

            useCase = new AddHotelierToHotelGroup(dataSource);
            useCase.execute();
            statusCode = useCase.succeeded() ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            responseHelper.respond(useCase.getResult(), statusCode);
        } else {
            responseHelper.error("POST /hotel_group_hoteliers must include 'hotel_group_hotelier'", HttpServletResponse.SC_BAD_REQUEST);
        }
        return null;
    }

    public Void handleDelete() throws Exception {
        if (requestHelper.body().has("hotel_group_hotelier")) {
            HotelGroupHotelier hgh = null;
            ArrayList<HotelGroupHotelier> hotel_group_hoteliers = null;
            HotelGroupHotelierSearchCriteria hgh_criteria = new HotelGroupHotelierSearchCriteria();

            JSONObject nestedJsonObject = requestHelper.body().getJSONObject("hotel_group_hotelier");

            if (nestedJsonObject.has("hotelier_id"))
                hgh_criteria.setHotelierID(nestedJsonObject.getInt("hotelier_id"));

            try {
                hotel_group_hoteliers = dataSource.findBySearchCriteria(HotelGroupHotelier.class,hgh_criteria);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(hotel_group_hoteliers.size()==0)
            {
                throw new Exception("Delete failed (no hotel group hotelier with id found)");
            }

           hgh = hotel_group_hoteliers.get(0);
            try {
                hgh.remove();
            } catch (UoWException e) {
                e.printStackTrace();
            }

            useCase = new RemoveHotelierFromHotelGroup(dataSource);
            useCase.execute();
            statusCode = useCase.succeeded() ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            responseHelper.respond(useCase.getResult(), statusCode);
        } else {
            responseHelper.error("DELETE/hotel_group_hoteliers must include 'hotel_group_hotelier'", HttpServletResponse.SC_BAD_REQUEST);
        }
        return null;
    }

    public HotelGroupHotelier getHotelGroupHotelierFromJsonObject(JSONObject jsonObject) {

        HotelGroupHotelier hg = null;
        Integer hotelier_id = null;
        Integer hotel_group_id = null;

        if(jsonObject.has("hotel_group_hotelier")) {
            JSONObject nestedJsonObject = jsonObject.getJSONObject("hotel_group_hotelier");

            if(nestedJsonObject.has("hotelier_id"))
                hotelier_id = nestedJsonObject.getInt("hotelier_id");
            if(nestedJsonObject.has("hotel_group_id"))
                hotel_group_id = nestedJsonObject.getInt("hotel_group_id");

            try {
                hg = new HotelGroupHotelier(dataSource,hotelier_id,hotel_group_id);
            } catch (UoWException e) {
                e.printStackTrace();
            }
        }
        return hg;
    }
}
