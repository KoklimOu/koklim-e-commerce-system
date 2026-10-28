package kh.gov.mptc.koklim.ecommerce.user.exception;

import kh.gov.mptc.koklim.ecommerce.common.restapi.dto.RestApiErrorResponse;
import kh.gov.mptc.koklim.ecommerce.common.restapi.exception.GlobalExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Validation errors (400) are inherited from GlobalExceptionHandler
@RestControllerAdvice
public class UserGlobalExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestApiErrorResponse<?> handleUserNotFound(UserNotFoundException e) {
        return RestApiErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(e.getMessage())
                .build();
    }

    // Keycloak problems: the exception carries the status we answer with (404, 409, 400 or 502)
    @ExceptionHandler(KeycloakAdminException.class)
    public ResponseEntity<RestApiErrorResponse<?>> handleKeycloakAdmin(KeycloakAdminException e) {
        return ResponseEntity.status(e.getStatus()).body(RestApiErrorResponse.builder()
                .code(e.getStatus().getReasonPhrase())
                .message(e.getMessage())
                .build());
    }
}
