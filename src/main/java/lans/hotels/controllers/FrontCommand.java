package lans.hotels.controllers;

import lans.hotels.api.IFrontCommand;
import lans.hotels.domain.IDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class FrontCommand implements IFrontCommand  {
    protected IDataSource dataSource;
    protected ServletContext context;
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    public void init(ServletContext context,
                     HttpServletRequest request,
                     HttpServletResponse response,
                     IDataSource dataSource) {
        this.context = context;
        this.request = request;
        this.response = response;
        this.dataSource = dataSource;
    }

    abstract public void process() throws ServletException, IOException;
}
