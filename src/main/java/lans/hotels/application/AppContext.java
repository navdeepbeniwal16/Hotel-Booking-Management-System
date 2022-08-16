package lans.hotels.application;

import lans.hotels.datasource.DBConnection;
import lans.hotels.environment.Environment;
import lans.hotels.environment.InvalidEnvironmentException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContext implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        System.out.println("Starting up ServletContext!");
        ServletContext ctx = contextEvent.getServletContext();
        Environment environment;
        try {
            environment = new Environment(System.getenv());
            DBConnection dbConnection = new DBConnection(environment.getDBConfiguration());
            ctx.setAttribute("DBConnection", dbConnection);
        } catch (InvalidEnvironmentException invalidEnvironmentException) {
            System.out.println(invalidEnvironmentException);
            System.exit(1); // TODO: centralise error codes
        }
    }
}
