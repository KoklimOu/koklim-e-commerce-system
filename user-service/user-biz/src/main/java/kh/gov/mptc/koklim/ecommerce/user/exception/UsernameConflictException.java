package kh.gov.mptc.koklim.ecommerce.user.exception;

import kh.gov.mptc.koklim.ecommerce.common.restapi.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class UsernameConflictException extends GlobalException {
    public UsernameConflictException(HttpStatus status, String message) {
        super(status, message);
    }
}
