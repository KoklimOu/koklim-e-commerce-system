package kh.gov.mptc.koklim.ecommerce.common.restapi.exception;

import org.springframework.http.HttpStatus;

// Base class for errors that carry their own HTTP status.
// Throw a subclass (or this class) from a service; GlobalExceptionHandler turns it into the JSON error response.
public class GlobalException extends RuntimeException {

    private final HttpStatus status;

    public GlobalException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
