package kh.gov.mptc.koklim.ecommerce.user.exception;

import org.springframework.http.HttpStatus;

// Something went wrong while talking to Keycloak. "status" is what OUR API answers with
// (404, 409, 400, or 502 when Keycloak is down / the admin client is misconfigured).
public class KeycloakAdminException extends RuntimeException {

    private final HttpStatus status;

    public KeycloakAdminException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
