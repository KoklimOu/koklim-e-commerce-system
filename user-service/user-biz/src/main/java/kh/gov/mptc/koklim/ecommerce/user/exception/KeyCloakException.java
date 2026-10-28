package kh.gov.mptc.koklim.ecommerce.user.exception;

import kh.gov.mptc.koklim.ecommerce.common.restapi.exception.GlobalException;
import org.springframework.http.HttpStatus;

/** Something went wrong talking to Keycloak (down, refused our admin client, user not found, ...). */
public class KeycloakException extends GlobalException {

    public KeycloakException(HttpStatus status, String message) {
        super(status, message);
    }
}
