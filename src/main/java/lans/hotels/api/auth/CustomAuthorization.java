package lans.hotels.api.auth;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Base64;

public class CustomAuthorization {
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
    Jwk jwk;
    DecodedJWT jwt;
    Algorithm algorithm;
    JWTVerifier verifier;
    boolean authenticated;
    JSONObject payload;
    String email;
    ArrayList<String> roles;
    private final static Base64.Decoder decoder = Base64.getUrlDecoder();

    protected CustomAuthorization(JwkProvider jwkProvider,
                                  String issuer,
                                  ArrayList<String> audiences,
                                  String namespace,
                                  HttpServletRequest request) {
        this.jwkProvider = jwkProvider;
        this.issuer = issuer;
        this.audiences = audiences;
        this.namespace = namespace;
        this.request = request;
        this.authenticated = false;
        email = "";
        roles = new ArrayList<>();
        processRequestAuth();
        request.getSession().setAttribute(AUTHORIZATION, this);
    }

    private CustomAuthorization(boolean authenticated, String email, ArrayList<String> roles) {
        this.authenticated = authenticated;
        this.email = email;
        this.roles = roles;
    }

    public static CustomAuthorization getAuthorization(HttpServletRequest request) {
        CustomAuthorization auth = (CustomAuthorization) request.getSession().getAttribute(CustomAuthorization.AUTHORIZATION);
        if (auth == null) auth = new CustomAuthorization(false, "", new ArrayList<>());
        return auth;
    }

    public String getEmail() {
        return email;
    }

    public boolean isCustomer() {
        return roles.contains(Roles.Customer.toString());
    }

    public boolean isHotelier() {
        return roles.contains(Roles.Hotelier.toString());
    }

    public boolean isAdmin() {
        return roles.contains(Roles.Admin.toString());
    }

    private void processRequestAuth() {
        try {
            String headerString = request.getHeader("Authorization");
            validateHeading(headerString);
            String tokenString = headerString.split(" ")[1];
            verifyTokenString(tokenString);
            processPayload();
        } catch (Exception e) {
            authenticated = false;
        }
    }

    private void processPayload() throws IllegalArgumentException{
        String decodedPayload = new String(decoder.decode(jwt.getPayload()));
        payload = new JSONObject(decodedPayload);
        email = (String) payload.get(namespace + "email");
        JSONArray rolesArray = (JSONArray) payload.get(namespace + "roles");
        for(Object role: rolesArray) roles.add((String) role);
    }

    private void verifyTokenString(String tokenString) throws JwkException {
        createVerifier(tokenString);
        jwt = verifier.verify(tokenString);
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

    private void validateHeading(String headerString) throws Exception {
        if (headerString==null || headerString.equals("")) {
            authenticated = false;
            throw new Exception("'Authorization' not on request");
        }
        String authorizationType = headerString.split(" ")[0];
        if (!authorizationType.equals("Bearer")) {
            authenticated = false;
            throw new Exception("Invalid 'Authorization' type - expected 'Bearer', receiver " + authorizationType);
        }
    }
}
