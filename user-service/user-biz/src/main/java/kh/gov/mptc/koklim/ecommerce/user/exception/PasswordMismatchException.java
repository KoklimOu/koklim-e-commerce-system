package kh.gov.mptc.koklim.ecommerce.user.exception;

import kh.gov.mptc.koklim.ecommerce.common.restapi.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends GlobalException {
    public PasswordMismatchException(HttpStatus status, String message) {
        super(status, message);
    }
}
