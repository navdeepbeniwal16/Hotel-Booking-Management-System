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
        checkCommandPath(2);
        switch (method) {
            case HttpMethod.GET:
                handleGet();
                return;
            case HttpMethod.POST:
                if (requestBody.has("search")) {
                    asAdmin(this::handleSearch);
                    return;
                } else if (requestBody.has("hotelier")) {
                    asAdmin(this::handlePostHotelier);
                    return;
                } else if (requestBody.has("customer")) {
                    asAdmin(this::handlePostCustomer);
                    return;
                }
            default:
                responder.unimplemented(request.getMethod() + " /users");
        }
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
            JSONObject nestedJsonObject = body.getJSONObject("customer");

            if(nestedJsonObject.has("email"))
                email = nestedJsonObject.getString("email");

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

    private Void handleGet() {
        useCase = new GetAllUsers(dataSource);
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responder.respond(useCase.getResult(), statusCode);
        return null;
    }

    private Void handleSearch() {
        UserSearchCriteria criteria = new UserSearchCriteria();
        JSONObject searchQueryBody = requestBody.getJSONObject("search");

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

        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responder.respond(useCase.getResult(), statusCode);
        return null;
    }

    private Void handlePostHotelier() throws Exception {
        User user = getUserFromJSONObject(requestBody);

        if (user == null)
            throw new InvalidObjectException("Failed to parse hotelier object from request body");


        useCase = new OnboardHotelier(dataSource);
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responder.respond(useCase.getResult(), statusCode);
        return null;
    }

    private Void handlePostCustomer() throws Exception {
        User user = getUserFromJSONObject(requestBody);

        if (user == null)
            throw new InvalidObjectException("Failed to parse customer object from request body");

        useCase = new CreateCustomer(dataSource);
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responder.respond(useCase.getResult(), statusCode);
        return null;
    }
}

