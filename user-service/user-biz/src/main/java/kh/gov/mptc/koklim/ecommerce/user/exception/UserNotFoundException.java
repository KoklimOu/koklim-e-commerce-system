package kh.gov.mptc.koklim.ecommerce.user.exception;

import kh.gov.mptc.koklim.ecommerce.common.restapi.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
