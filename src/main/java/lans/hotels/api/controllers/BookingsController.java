package lans.hotels.api.controllers;

import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelSearchCriteria;
import lans.hotels.datasource.search_criteria.UserSearchCriteria;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.user.User;
import lans.hotels.domain.utils.DateRange;
import lans.hotels.use_cases.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.*;

public class BookingsController extends FrontCommand {
    @Override
    protected void concreteProcess() throws Exception {
        System.out.println("BookingsController.concreteProcess(): " + request.getMethod() + " " + request.getRequestURI());
        switch (request.getMethod()) {
            case HttpMethod.GET:
                responseHelper.error("GET /bookings: NOT IMPLEMENTED", HttpServletResponse.SC_NOT_IMPLEMENTED);
                return;
            case HttpMethod.POST:
                asCustomerOrHotelier(this::handlePost);
                return;
            case HttpMethod.PUT:
                asCustomerOrHotelier(this::handlePut);
                return;
            default:
                responseHelper.unimplemented(request.getMethod() + " /bookings");
        }
    }

    public Void handlePost() throws Exception {
        if (requestHelper.body().has("booking")) {
            handleCreateNewBooking();
        }
        else if(requestHelper.body().has("search"))
        {
            JSONObject searchQuery = requestHelper.body().getJSONObject("search");
            handleSearchQuery(searchQuery);
        }
        return null;
    }

    private void handleCreateNewBooking() {
        try {
            auth.withGuard(this::createNewBookingGuard, this::createNewBooking);
        } catch (Exception e) {
            e.printStackTrace();
            responseHelper.internalServerError();
        }
    }

    private Boolean createNewBookingGuard() {
        Integer hotelId = (Integer) requestHelper.body("booking", "hotel_id");
        boolean authCheck = Boolean.FALSE;
        if (auth.isHotelier()) {
            authCheck = hotelId.equals(auth.getUser().getHotelierHotelGroupID());
        }
        else return true;
        return authCheck;
    }

    private Void createNewBooking() {

        useCase = new CreateBooking(dataSource, requestHelper.body("booking"), auth.getId());
        useCase.execute();
        responseHelper.respond(useCase.getResult(), HttpServletResponse.SC_OK);
        return null;
    }

    private void handleSearchQuery(JSONObject searchQuery) {
        if(searchQuery.has("hotel_id")) {
            Integer hotel_id = searchQuery.getInt("hotel_id");

            if(!checkHotelGroupHotelierValid(hotel_id))
            {
                responseHelper.unauthorized();
                return;
            }

            useCase = new ViewHotelGroupBookings(dataSource, hotel_id);
            useCase.execute();
            statusCode = useCase.succeeded() ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            responseHelper.respond(useCase.getResult(), statusCode);
        }

        if(searchQuery.has("customer_bookings")) {
            Integer customer_id = auth.getId();
            useCase = new ViewCustomerBookings(dataSource, customer_id);
            useCase.execute();
            statusCode = useCase.succeeded() ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            responseHelper.respond(useCase.getResult(), statusCode);
        }
    }

    private boolean checkHotelGroupHotelierValid(Integer h_id){
        Integer hotelier_hg_id = -1;
        Integer hotel_hg_id =-2;
        String hotelier_email = auth.getEmail();

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
            hotelier_hg_id = hgh.getHotelierHotelGroupID();
        }
        ArrayList<Hotel> hotels = null;

        HotelSearchCriteria h_criteria = new HotelSearchCriteria();
        h_criteria.setId(h_id);

        try {
            hotels = dataSource.findBySearchCriteria(Hotel.class, h_criteria);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(hotels.size() > 0) {
            Hotel h = hotels.get(0);
            hotel_hg_id = h.getHotelGroupID();
        }

        return hotelier_hg_id == hotel_hg_id;
    }

    public Void handlePut() throws Exception {
        if(requestHelper.body().has("booking")) {
            JSONObject bookingJsonBody = requestHelper.body().getJSONObject("booking");

            if(bookingJsonBody.has("id")) {
                Integer bookingId = bookingJsonBody.getInt("id");

                if(bookingId != null) {
                    ArrayList<Booking> bookings = null;
                    Booking booking = null;
                    BookingsSearchCriteria criteria = new BookingsSearchCriteria();
                    criteria.setBookingId(bookingId);
                    try {
                        bookings = dataSource.findBySearchCriteria(Booking.class, criteria);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(bookings.size() > 0)
                        booking = bookings.get(0);
                    else
                        throw new Exception("No booking with id found");

                    if(bookingJsonBody.has("cancel")) {
                        handleCancelBooking(booking);
                    }
                    else if(bookingJsonBody.has("start_date") && bookingJsonBody.has("end_date")) {
                        handleDateChange(bookingJsonBody,booking);
                        System.out.println("Check point 1");
                    }
                }
            }
        }
        return null;
    }

    private void handleCancelBooking(Booking booking) throws Exception {
        if (auth.isCustomer()) {
            if (booking.getCustomerId() != auth.getId()){
                responseHelper.unauthorized();
                return;
            }
        }
        else if (auth.isHotelier()) {
            ArrayList<Hotel> hotels = null;

            HotelSearchCriteria h_criteria = new HotelSearchCriteria();
            h_criteria.setId(booking.getHotelId());

            try {
                hotels = dataSource.findBySearchCriteria(Hotel.class, h_criteria);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Hotel h = null;
            if (hotels.size() > 0) {
                h = hotels.get(0);
            }
            if (auth.getUser().getHotelierHotelGroupID() != h.getHotelGroupID()){
                responseHelper.unauthorized();
                return;
            }

        }
        else
        {
            responseHelper.unauthorized();
            return;
        }


        useCase = new CancelBooking(dataSource,booking);
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responseHelper.respond(useCase.getResult(), statusCode);
    }

    public void handleDateChange(JSONObject bookingJsonBody, Booking booking) throws Exception {
        if (auth.isCustomer()) {
            if (booking.getCustomerId() != auth.getId()){
                responseHelper.unauthorized();
                return;
            }
        }

        String startDateString = bookingJsonBody.getString("start_date");
        String endDateString = bookingJsonBody.getString("end_date");
        java.sql.Date startDate = new Date(dateFormat.parse(startDateString).getTime());
        java.sql.Date endDate = new Date(dateFormat.parse(endDateString).getTime());

        useCase = new ChangeBookingDates(dataSource,booking, startDate, endDate);
        useCase.execute();
        statusCode = useCase.succeeded() ?
                HttpServletResponse.SC_OK :
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        responseHelper.respond(useCase.getResult(), statusCode);

    }
}
