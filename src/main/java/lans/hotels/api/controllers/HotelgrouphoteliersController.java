package lans.hotels.api.controllers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.HotelGroupHotelierSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelGroupSearchCriteria;
import lans.hotels.datasource.search_criteria.UserSearchCriteria;
import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.hotel_group.HotelGroupHotelier;
import lans.hotels.domain.user.User;
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
            default:
                responseHelper.unimplemented(request.getMethod() + " /hotelgrouphoteliers");
        }
    }

    public Void handlePost() throws Exception {
        if (requestHelper.body().has("hotel_group_hotelier")) {
            HotelGroupHotelier hgh = getHotelGroupHotelierFromJsonObject(requestHelper.body());

            if (hgh == null) {
                return null;
            }

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

            if (nestedJsonObject.has("hotelier_id")) {
                hgh_criteria.setHotelierID(nestedJsonObject.getInt("hotelier_id"));

                try {
                    hotel_group_hoteliers = dataSource.findBySearchCriteria(HotelGroupHotelier.class, hgh_criteria);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (hotel_group_hoteliers.size() == 0) {
                    responseHelper.error("DELETE /hotel_group_hoteliers entry does not exist", HttpServletResponse.SC_BAD_REQUEST);
                    return null;
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
            }
            else
                responseHelper.error("DELETE /hotel_group_hoteliers must include 'hotelier_id'", HttpServletResponse.SC_BAD_REQUEST);
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
            else {
                responseHelper.error("POST /hotel_group_hoteliers must include 'hotelier_id'", HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
            if(nestedJsonObject.has("hotel_group_id"))
                hotel_group_id = nestedJsonObject.getInt("hotel_group_id");
            else {
                responseHelper.error("POST /hotel_group_hoteliers must include 'hotel_group_id'", HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }

            try {

                ArrayList<User> users = null;
                UserSearchCriteria u_criteria = new UserSearchCriteria();
                u_criteria.setId(hotelier_id);
                try {
                    users = dataSource.findBySearchCriteria(User.class, u_criteria);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (users.size()==0) {
                    responseHelper.error("POST /hotelgrouphoteliers user id does not exist", HttpServletResponse.SC_BAD_REQUEST);
                    return null;
                }
                for (int i=0;i<users.size();i++)
                {
                    if(users.get(i).getId()==hotelier_id){
                        if(!users.get(i).getRole().isHotelier())
                        {
                            responseHelper.error("POST /hotelgrouphoteliers user id is not a hotelier", HttpServletResponse.SC_BAD_REQUEST);
                            return null;
                        }
                        else if(users.get(i).getHotelierHotelGroupID()!=0)
                        {
                            responseHelper.error("POST /hotelgrouphoteliers user id already associated to hotel group", HttpServletResponse.SC_BAD_REQUEST);
                            return null;
                        }
                    }
                }

                ArrayList<HotelGroup> hotel_groups = null;
                HotelGroupSearchCriteria hg_criteria = new HotelGroupSearchCriteria();
                hg_criteria.setHotelGroupID(hotel_group_id);
                try {
                    hotel_groups = dataSource.findBySearchCriteria(HotelGroup.class, hg_criteria);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (hotel_groups.size()==0) {
                    responseHelper.error("POST /hotelgrouphoteliers hotel group id does not exist", HttpServletResponse.SC_BAD_REQUEST);
                    return null;
                }

                hg = new HotelGroupHotelier(dataSource,hotelier_id,hotel_group_id);
            } catch (UoWException e) {
                e.printStackTrace();
            }
        }
        return hg;
    }
}
