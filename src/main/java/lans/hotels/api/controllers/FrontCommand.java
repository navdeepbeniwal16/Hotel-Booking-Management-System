package lans.hotels.api.controllers;

import lans.hotels.api.utils.RequestHelper;
import lans.hotels.api.utils.ResponseHelper;
import lans.hotels.api.entrypoint.IFrontCommand;
import lans.hotels.api.auth.Auth0Adapter;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user.Role;
import lans.hotels.use_cases.UseCase;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

public abstract class FrontCommand implements IFrontCommand  {
    protected IDataSource dataSource;
    protected ServletContext context;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String method;
    protected Auth0Adapter auth;
    protected UseCase useCase;
    protected DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    protected int statusCode;

    protected ResponseHelper responseHelper;
    protected RequestHelper requestHelper;

    private boolean ready;

    public void init(ServletContext context,
                     HttpServletRequest request,
                     HttpServletResponse response,
                     IDataSource dataSource) {
        try {
            this.context = context;
            this.request = request;
            this.method = request.getMethod();
            this.response = response;
            this.dataSource = dataSource;

            this.responseHelper = new ResponseHelper(response, dataSource);
            this.requestHelper = new RequestHelper(request);
            this.auth = Auth0Adapter.getAuthorization(request, responseHelper::unauthorized);
        } catch (Exception e) {
            ready = false;
        }
    }

    abstract protected void concreteProcess() throws Exception;
    public void process() {
        try {
            assert ready;
            concreteProcess();
        } catch (Exception e) {
            responseHelper.internalServerError();
            e.printStackTrace();
        }

    }


    private <T> T delegateToAuth(List<Role> roles, Callable<T> handler) {
        try {
            return auth.inRoles(roles, handler);
        } catch (Exception e ){
            responseHelper.internalServerError("auth error");
        }
        return null;
    }


    protected <T> T asAdmin(Callable<T> handler) {
        return delegateToAuth(List.of(Role.admin()), handler);
    }

    protected <T> T asHotelier(Callable<T> handler) {
        return delegateToAuth(List.of(Role.hotelier()), handler);
    }
    protected <T> T asCustomer(Callable<T> handler) {
        return delegateToAuth(List.of(Role.customer()), handler);
    }

    protected <T> T asCustomerOrHotelier(Callable<T> handler) {
        return delegateToAuth(List.of(Role.customer(), Role.hotelier()), handler);
    }

    protected <T> T asHotelierOrAdmin(Callable<T> handler) {
        return delegateToAuth(List.of(Role.admin(), Role.hotelier()), handler);
    }
}
