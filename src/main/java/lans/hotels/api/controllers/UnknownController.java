package lans.hotels.api.controllers;

import javax.servlet.http.HttpServletResponse;
public class UnknownController extends FrontCommand {
    @Override
    public void concreteProcess() {
        responseHelper.error(requestHelper.methodAndURI(), HttpServletResponse.SC_NOT_FOUND);
    }
}
