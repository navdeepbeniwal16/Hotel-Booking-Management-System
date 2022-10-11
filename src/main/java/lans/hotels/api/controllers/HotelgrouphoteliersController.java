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
        String[] commandPath = request.getPathInfo().split("/");
        int statusCode;

        switch(request.getMethod()) {
            case HttpMethod.GET:
            case HttpMethod.POST: {
                if (requestBody.has("hotel_group_hotelier")) {
                    HotelGroupHotelier hgh = getHotelGroupHotelierFromJsonObject(requestBody);

                    if (hgh == null)
                        throw new InvalidObjectException("Failed to parse hotel object from request body");

                    if (!auth.isAdmin()) {
                        unauthorized();
                        return;
                    }
                    useCase = new AddHotelierToHotelGroup(dataSource);
                    useCase.execute();
                    statusCode = useCase.succeeded() ?
                            HttpServletResponse.SC_OK :
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                    sendJsonResponse(response, useCase.getResult(), statusCode);
                    return;
                } else {
                    System.err.println("POST /api/hotels: " + Arrays.toString(commandPath));
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                    return;
                }
            }
            case HttpMethod.PUT:
                return;
            case HttpMethod.DELETE: {
                if (!auth.isAdmin()) {
                    unauthorized();
                    return;
                }

                if (requestBody.has("hotel_group_hotelier")) {
                    HotelGroupHotelier hgh = null;
                    ArrayList<HotelGroupHotelier> hotel_group_hoteliers = null;
                    HotelGroupHotelierSearchCriteria hgh_criteria = new HotelGroupHotelierSearchCriteria();

                    JSONObject nestedJsonObject = requestBody.getJSONObject("hotel_group_hotelier");

                    if (nestedJsonObject.has("hotelier_id"))
                        hgh_criteria.setHotelierID(nestedJsonObject.getInt("hotelier_id"));


                    try {
                        hotel_group_hoteliers = dataSource.findBySearchCriteria(HotelGroupHotelier.class,hgh_criteria);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("Results found  = "+hotel_group_hoteliers.size());

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
                    sendJsonResponse(response, useCase.getResult(), statusCode);
                    return;
                } else {
                    System.err.println("POST /api/hotels: " + Arrays.toString(commandPath));
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request.getRequestURI());
                    return;
                }
            }
        }
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
