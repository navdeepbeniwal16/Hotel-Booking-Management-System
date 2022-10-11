package lans.hotels.api.auth;

import com.auth0.jwk.JwkProvider;
import lans.hotels.domain.IDataSource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class AuthorizationFactory {
    JwkProvider jwkProvider;
    String issuer;
    ArrayList<String> audiences;
    String namespace;

    public AuthorizationFactory(JwkProvider jwkProvider) {
        this.jwkProvider = jwkProvider;
        audiences = new ArrayList<>();
    }

    public void withAudience(String audience) {
        this.audiences.add(audience);
    }

    public void withIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void withNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Auth0Adapter injectAuthorization(HttpServletRequest request, IDataSource dataSource) {
        return new Auth0Adapter(jwkProvider, issuer, audiences, namespace, request, dataSource);
    }
}
