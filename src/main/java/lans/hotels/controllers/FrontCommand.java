package lans.hotels.controllers;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.SessionUtils;
import com.auth0.Tokens;
import lans.hotels.api.IFrontCommand;
import lans.hotels.api.exceptions.CommandException;
import lans.hotels.domain.IDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

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

    public void process() throws ServletException, IOException, CommandException, SQLException {
        if (context == null || request == null || response == null || dataSource == null) {
            throw new CommandException(this.getClass() + " must be initialised by it can process a command.");
        }
        concreteProcess();
    }

    abstract protected void concreteProcess() throws CommandException, IOException, SQLException;

//    protected void handleAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        AuthenticationController authController = (AuthenticationController) context.getAttribute("AuthenticationController");
//        try {
//            // TODO: removing access token when session ended?
//            System.out.println("1");
//            Tokens tokens = authController.handle(request, response);
//            System.out.println("2");
//            if (tokens.getAccessToken() != null && tokens.getIdToken() != null) {
//                System.out.println("3");
//                SessionUtils.set(request, "accessToken", tokens.getAccessToken());
//                System.out.println("4");
//                SessionUtils.set(request, "idToken", tokens.getIdToken());
//                System.out.println("User access token:");
//                System.out.println(tokens.getAccessToken());
//                System.out.println("User ID token:");
//                System.out.println(tokens.getIdToken());
//            }
//        } catch (IdentityVerificationException e) {
//            e.printStackTrace();
//            System.out.println("handleAuth: " + e);
//            SessionUtils.set(request, "accessToken", null);
//            SessionUtils.set(request, "idToken", null);
//        }
//    }
}
