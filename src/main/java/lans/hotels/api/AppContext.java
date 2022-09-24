package lans.hotels.api;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import lans.hotels.api.auth.AuthorizationFactory;
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
        String auth0ClientId = ctx.getInitParameter("com.auth0.clientId");
        String auth0ClientSecret = ctx.getInitParameter("com.auth0.clientSecret");
        String auth0Audience = ctx.getInitParameter("com.auth0.audience");

        if (auth0Domain == null || auth0ClientId == null || auth0ClientSecret == null || auth0Audience == null) {
            System.err.println("CONTEXT ERROR: invalid auth0 configuration");
            System.out.println("\t" + auth0Domain);
            System.out.println("\t" + auth0ClientId);
            System.out.println("\t" + auth0ClientSecret);
            System.out.println("\t" + auth0Audience);
            System.exit(1);
        }

        System.out.println("Auth0:");
        System.out.println("\tDomain: " + auth0Domain);
        System.out.println("\tClient ID: " + auth0ClientId);
        System.out.println("\tClient Secret: " + auth0ClientSecret);
        System.out.println("\tAudience: " + auth0Audience);

        try {
            // Database connection based on environment (dev vs prod)
            environment = new Environment(System.getenv());
            dbConnection = new PostgresConnection(environment.getDBConfiguration());
            ctx.setAttribute("DBConnection", dbConnection);

            // Authentication and authorisation using Auth0
            JwkProvider jwkProvider = new JwkProviderBuilder(auth0Domain).build();
            AuthorizationFactory authFactory = new AuthorizationFactory(jwkProvider);
            authFactory.withAudience("https://swen90007-2022-lans.herokuapp.com/api");
            authFactory.withAudience("https://dev-easqepri.us.auth0.com/userinfo");
            authFactory.withIssuer("https://dev-easqepri.us.auth0.com/");
            authFactory.withNamespace("lans_hotels/");
            ctx.setAttribute("AuthFactory", authFactory);
        } catch (InvalidEnvironmentException invalidEnvironmentException) {
            System.err.println("AppContext | InvalidEnvironmentException: " + invalidEnvironmentException.getMessage());
            System.err.println(invalidEnvironmentException.getMessage());
            System.exit(1); // TODO: centralise error codes
        }
    }
}
