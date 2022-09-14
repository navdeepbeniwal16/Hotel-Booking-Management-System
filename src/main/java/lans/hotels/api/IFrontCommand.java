package lans.hotels.api;

import lans.hotels.api.exceptions.CommandException;
import lans.hotels.domain.IDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IFrontCommand {
    void process() throws ServletException, IOException, CommandException;
    void init(ServletContext context,
              HttpServletRequest request,
              HttpServletResponse response,
              IDataSource dataSource);
}
