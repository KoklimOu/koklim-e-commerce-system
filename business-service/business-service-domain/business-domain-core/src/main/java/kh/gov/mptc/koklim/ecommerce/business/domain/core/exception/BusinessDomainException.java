package kh.gov.mptc.koklim.ecommerce.business.domain.core.exception;

import kh.gov.mptc.koklim.ecommerce.common.domain.exception.DomainException;

public class BusinessDomainException extends DomainException {

    public BusinessDomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessDomainException(String message) {
        super(message);
    }
}
