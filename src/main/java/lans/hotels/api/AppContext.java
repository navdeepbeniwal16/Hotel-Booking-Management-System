package lans.hotels.api;

import com.auth0.AuthenticationController;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import lans.hotels.datasource.connections.DBConnection;
import lans.hotels.datasource.connections.PostgresConnection;
import lans.hotels.environment.Environment;
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

        String auth0Domain = ctx.getInitParameter("com.auth0.domain");
        String auth0ClientId = ctx.getInitParameter("com.auth0.domain");
        String auth0ClientSecret = ctx.getInitParameter("com.auth0.clientSecret");

        if (auth0Domain == null || auth0ClientId == null || auth0ClientSecret == null) {
            System.err.println("CONTEXT ERROR: invalid auth0");
            System.out.println("\t" + auth0Domain);
            System.out.println("\t" + auth0ClientId);
            System.out.println("\t" + auth0ClientSecret);
            System.exit(1);
        }

        JwkProvider jwkProvider = new JwkProviderBuilder(auth0Domain).build();
        AuthenticationController controller = AuthenticationController
                .newBuilder(auth0Domain, auth0ClientId, auth0ClientSecret)
                .withJwkProvider(jwkProvider)
                .build();

        System.out.println("Auth0:");
        System.out.println("\t" + auth0Domain);
        System.out.println("\t" + auth0ClientId);
        System.out.println("\t" + auth0ClientSecret);
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
