package lans.hotels.api.entrypoint;

import lans.hotels.api.utils.Responder;
import lans.hotels.api.exceptions.CommandException;
import lans.hotels.domain.IDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public interface IFrontCommand {
    void process() throws ServletException, IOException, CommandException, SQLException;
    void init(ServletContext context,
              HttpServletRequest request,
              HttpServletResponse response,
              IDataSource dataSource,
              Responder responder);
}
