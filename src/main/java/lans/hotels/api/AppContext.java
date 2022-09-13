package lans.hotels.api;

import lans.hotels.datasource.connections.DBConnection;
import lans.hotels.datasource.connections.PostgresConnection;
import lans.hotels.environment.Environment;
import lans.hotels.environment.EnvironmentI;
import lans.hotels.environment.InvalidEnvironmentException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContext implements ServletContextListener {
    private DBConnection dbConnection;
    private EnvironmentI environment;
    private ServletContext ctx;
    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        System.out.println("Starting up ServletContext!");
        ctx = contextEvent.getServletContext();
        try {
            environment = new Environment(System.getenv());
            dbConnection = new PostgresConnection(environment.getDBConfiguration());
            ctx.setAttribute("DBConnection", dbConnection);
        } catch (InvalidEnvironmentException invalidEnvironmentException) {
            System.out.println(invalidEnvironmentException);
            System.exit(1); // TODO: centralise error codes
        }
    }
}
