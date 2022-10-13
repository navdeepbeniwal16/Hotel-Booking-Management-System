package lans.hotels.api.auth;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import lans.hotels.datasource.search_criteria.UserSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user.Role;
import lans.hotels.domain.user.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Callable;

public class Auth0Adapter {
    public static final String AUTHORIZATION = "Authorization";
    private enum Roles {
        Customer,
        Hotelier,
        Admin
    }

    JwkProvider jwkProvider;
    String issuer;
    ArrayList<String> audiences;
    String namespace;

    HttpServletRequest request;
    RSAPublicKey publicKey;
    IDataSource dataSource;
    Jwk jwk;
    DecodedJWT jwt;
    Algorithm algorithm;
    JWTVerifier verifier;
    boolean authenticated;
    JSONObject payload;
    String email;
    ArrayList<String> roles;
    User user;
    String rawAuth;
    String rawToken;
    private final static Base64.Decoder decoder = Base64.getUrlDecoder();

    protected Auth0Adapter(JwkProvider jwkProvider,
                           String issuer,
                           ArrayList<String> audiences,
                           String namespace,
                           HttpServletRequest request,
                           IDataSource dataSource) {
        this.jwkProvider = jwkProvider;
        this.issuer = issuer;
        this.audiences = audiences;
        this.namespace = namespace;
        this.request = request;
        this.authenticated = false;
        this.dataSource = dataSource;
        this.user = null;
        email = "";
        roles = new ArrayList<>();
        rawAuth = "";
        rawToken = "";
        processRequestAuth();
        request.getSession().setAttribute(AUTHORIZATION, this);
        System.out.println(this);
    }

    private Auth0Adapter() {
        this.authenticated = false;
        email = "";
        this.roles = new ArrayList<>();
    }

    public String toString() {
        return "auth0(authenticated=" + authenticated +
                " | email=" + email +
                " | role claims=" + roles.toString() +
                " | admin=" + isAdmin() +
                " | hotelier=" + isHotelier() +
                " | customer=" + isCustomer() + ")";
    }

    public static Auth0Adapter getAuthorization(HttpServletRequest request) {
        Auth0Adapter auth = (Auth0Adapter) request.getSession().getAttribute(Auth0Adapter.AUTHORIZATION);
        if (auth == null) auth = new Auth0Adapter();
        return auth;
    }

    public String getEmail() {
        return email;
    }

    public boolean isCustomer() {
        return authenticated && !isHotelier() && !isAdmin();
    }

    public boolean isHotelier() {
        // TODO: refactor to user role from user
        return authenticated && roles.contains(Roles.Hotelier.toString());
    }

    public boolean isAdmin() {
        // TODO: refactor to user role from user
        return authenticated && user.getRole().isAdmin();
    }

    private void processRequestAuth() {
        rawAuth = request.getHeader("Authorization");
        try {
            authenticateWithHeading();
        } catch (Exception e) {
            // Override any method that may have set auth to true
            resetAuth();
        }
    }

    private void authenticateWithHeading() throws Exception {
        if (validHeading()) {
            setJwt();
            processPayload();
        }
    }

    private void processPayload() throws Exception {
        String decodedPayload = new String(decoder.decode(jwt.getPayload()));
        payload = new JSONObject(decodedPayload);
        setEmail();
        setRoles();
        setUser();
    }

    private void setEmail() {
        email = (String) payload.get(namespace + "email");
    }

    private void setRoles() {
        JSONArray rolesArray = (JSONArray) payload.get(namespace + "roles");
        for(Object role: rolesArray) roles.add((String) role); // Refactor: hook up role assignment to backend.
    }

    private void setUser() throws Exception {
        if (authenticated) {
            UserSearchCriteria emailCriteria = new UserSearchCriteria();
            emailCriteria.setEmail(email);
            ArrayList<User> users = dataSource.findBySearchCriteria(User.class, emailCriteria);
            if (users.isEmpty()) {
                System.out.println("Creating new user:\n" +
                        "\temail: " + email +
                        "\troles: " + roles.toString());

                user = roles.contains(Roles.Admin.toString()) ? User.newAdmin(dataSource, email) : User.newCustomer(dataSource, email);
            } else {
                user = users.get(0);
            }
            System.out.println("User:\t" + user.getEmail());
        }
    }

    private void setJwt() throws JwkException {
        rawToken = rawAuth.split(" ")[1];
        createVerifier(rawToken);
        jwt = verifier.verify(rawToken);
        authenticated = true;
    }

    private void createVerifier(String tokenString) throws JwkException {
        DecodedJWT decodedJwt = JWT.decode(tokenString);
        jwk = jwkProvider.get(decodedJwt.getKeyId());
        publicKey = (RSAPublicKey) jwk.getPublicKey();
        algorithm = Algorithm.RSA256(publicKey, null);
        Verification verification = JWT.require(algorithm);
        verification.withIssuer(issuer);
        audiences.forEach(verification::withAudience);
        verifier = verification.build();
    }

    private boolean validHeading() throws Exception {
        if (rawAuth==null || rawAuth.equals("")) {
            authenticated = false;
//            throw new Exception("'Authorization' not on request");
            return true;
        }
        String authorizationType = rawAuth.split(" ")[0];
        if (!authorizationType.equals("Bearer")) {
            authenticated = false;
            return false;
        }
        return true;
    }

    private void resetAuth() {
        authenticated = false;
        user = null;
        email = "";
        roles = new ArrayList<>();
        rawAuth = "";
        rawToken = "";
    }

    public <T, U> T asAdmin(Callable<T> handler, Callable<U> onUnauthorized) throws Exception {
        return withGuard(this::isAdmin, handler, onUnauthorized);
    }

    public <T, U> T asCustomer(Callable<T> handler, Callable<U> onUnauthorized) throws Exception {
        return withGuard(this::isCustomer, handler, onUnauthorized);
    }

    public <T, U> T asHotelier(Callable<T> handler, Callable<U> onUnauthorized) throws Exception {
        return withGuard(this::isCustomer, handler, onUnauthorized);
    }

    private Boolean inRolesGuard(List<Role> roles) {
        return authenticated &&
                user.hasRole() &&
                user.hasId() &&
                roles.contains(user.getRole());
    }

    public <T, U> T inRoles(List<Role> roles, Callable<T> handler, Callable<U> onUnauthorized) throws Exception {
        return withGuard(() -> inRolesGuard(roles), handler, onUnauthorized);
    }

    private <T, U> T withGuard(Callable<Boolean> guard,
                         Callable<T> action,
                         Callable<U> onUnauthorized) throws Exception {
        if (guard.call()) {
            return action.call();
        } else {
            onUnauthorized.call();
            return null;
        }
    }

    public Integer getId() {
        if (authenticated && user != null && user.getId() != null)
            return user.getId();
        return -1;
    }
}
