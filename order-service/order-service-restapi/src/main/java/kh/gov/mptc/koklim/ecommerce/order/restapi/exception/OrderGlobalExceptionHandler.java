package kh.gov.mptc.koklim.ecommerce.order.restapi.exception;

import kh.gov.mptc.koklim.ecommerce.common.restapi.dto.RestApiErrorResponse;
import kh.gov.mptc.koklim.ecommerce.common.restapi.exception.GlobalExceptionHandler;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.exception.OrderDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//Ever error that related to rest will fall here
@RestControllerAdvice
public class OrderGlobalExceptionHandler extends GlobalExceptionHandler {

    // A broken business rule is the client's fault (bad customer, bad price...), not a server error
    @ExceptionHandler(OrderDomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestApiErrorResponse<?> handleOrderDomainException(OrderDomainException e) {
        return RestApiErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .build();
    }
}
