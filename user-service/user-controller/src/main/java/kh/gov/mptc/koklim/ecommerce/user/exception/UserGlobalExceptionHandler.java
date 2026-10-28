package kh.gov.mptc.koklim.ecommerce.user.exception;

import kh.gov.mptc.koklim.ecommerce.common.restapi.exception.GlobalExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Every error from this service is handled here. The handlers (validation 400, GlobalException with its own status)
// are inherited from GlobalExceptionHandler in common-restapi.
@RestControllerAdvice
public class UserGlobalExceptionHandler extends GlobalExceptionHandler {
}
