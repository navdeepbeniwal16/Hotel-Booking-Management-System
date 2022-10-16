package lans.hotels.api.controllers;
import lans.hotels.datasource.search_criteria.UserSearchCriteria;
import lans.hotels.domain.user.Role;
import lans.hotels.domain.user.User;
import lans.hotels.use_cases.CreateCustomer;
import lans.hotels.use_cases.GetAllHoteliers;
import lans.hotels.use_cases.GetAllUsers;
import lans.hotels.use_cases.OnboardHotelier;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.InvalidObjectException;

public class UsersController extends FrontCommand {
    @Override
    protected void concreteProcess() {
        requestHelper.checkCommandPath(2);
        switch (method) {
            case HttpMethod.GET:
                asAdmin(this::handleGet);
                return;
            case HttpMethod.POST:
                handlePost();
                return;
            default:
                responseHelper.unimplemented(request.getMethod() + " /users");
        }
    }

    public void handlePost() {
        if (requestHelper.body().has("search")) {
            asAdmin(this::handleSearch);
        } else if (requestHelper.body().has("hotelier")) {
            asAdmin(this::handlePostHotelier);
        } else if (requestHelper.body().has("customer")) {
            asCustomer(this::handlePostCustomer);
        } else if (requestHelper.body().has("new")) {
            handleNewUser();
        }
        else
            responseHelper.error("POST /users must contain search, hotelier, customer or new", HttpServletResponse.SC_BAD_REQUEST);
    }

    private void handleNewUser() {
        JSONObject res = new JSONObject();
        res.put("success", auth.isAuthenticated());
        res.put("role", auth.getUser().getRole().getName());
        res.put("group", auth.getUser().getHotelierHotelGroupID());
        res.put("id", auth.getUser().getId());
        res.put("name", auth.getUser().getName());
        res.put("email", auth.getUser().getEmail());
        responseHelper.respondOK(res);
    }

    public User getUserFromJSONObject(JSONObject body) {

        User user = null;
        String email = null;

        if(body.has("hotelier")) {
            JSONObject nestedJsonObject = body.getJSONObject("hotelier");

            if(nestedJsonObject.has("email"))
                email = nestedJsonObject.getString("email");

            Role role = null;
            try {
                role = new Role(2);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                user = new User(dataSource,null,email,null,role,null,null,null,null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(body.has("customer")) {
            email = auth.getEmail();

            Role role = null;
            try {
                role = new Role(3);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                user = new User(dataSource,null,email,null,role,null,null,null,null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    private Void handleGet() throws Exception {
        useCase = new GetAllUsers(dataSource);
        useCase.execute(
                () -> responseHelper.respondOK(useCase.getResult()),
                () -> responseHelper.internalServerError());
        return null;
    }

    private Void handleSearch() throws Exception {
        UserSearchCriteria criteria = new UserSearchCriteria();
        JSONObject searchQueryBody = requestHelper.body().getJSONObject("search");

        if (searchQueryBody.has("id")) {
            Integer userID = searchQueryBody.getInt("id");
            if (userID != null) criteria.setId(userID);
        }
        if (searchQueryBody.has("role")) {
            Integer role = searchQueryBody.getInt("role");
            Role r = null;
            try {
                r = new Role(role);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (role != null) criteria.setRole(r);
        }

        String userType = "";
        if (searchQueryBody.has("type")) {
            userType = searchQueryBody.getString("type");
        }

        if (userType.equals("hotelier")) {
            useCase = new GetAllHoteliers(dataSource);
        } else {
            useCase = new GetAllUsers(dataSource);
        }

        useCase.execute(() -> responseHelper.respondOK(useCase.getResult()), () -> responseHelper.internalServerError("use case error"));
        return null;
    }

    private Void handlePostHotelier() throws Exception {
        JSONObject hotelierData = (JSONObject) requestHelper.body().get("hotelier");

        if(hotelierData.has("id")) {
            Integer userID = hotelierData.getInt("id");

            User user = dataSource.find(User.class, userID);
            if (user != null) {
                if (user.getRole().isCustomer())
                    user.setRole(Role.hotelier());
                else {
                    responseHelper.error("User is not a customer", HttpServletResponse.SC_BAD_REQUEST);
                    return null;
                }
            } else{
                responseHelper.error("User with id does not exist", HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }

            useCase = new OnboardHotelier(dataSource);
            useCase.execute(() -> responseHelper.respondOK(useCase.getResult()), () -> responseHelper.internalServerError("use case error"));
        }
        else
            responseHelper.error("POST /users hotelier must contain id", HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    private Void handlePostCustomer() throws Exception {
        User user = getUserFromJSONObject(requestHelper.body());

        if (user == null)
            throw new InvalidObjectException("Failed to parse customer object from request body");

        useCase = new CreateCustomer(dataSource);
        useCase.execute(() -> responseHelper.respondOK(useCase.getResult()), () -> responseHelper.internalServerError("use case error"));
        return null;
    }
}

