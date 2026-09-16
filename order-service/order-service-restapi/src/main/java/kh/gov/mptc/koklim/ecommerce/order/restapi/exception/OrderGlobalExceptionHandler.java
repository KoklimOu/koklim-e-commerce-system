package kh.gov.mptc.koklim.ecommerce.order.restapi.exception;

import kh.gov.mptc.koklim.ecommerce.restapi.exception.GlobalExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//Ever error that related to rest will fall here
@RestControllerAdvice
public class OrderGlobalExceptionHandler extends GlobalExceptionHandler {
    //todo write your own exception handler
}
