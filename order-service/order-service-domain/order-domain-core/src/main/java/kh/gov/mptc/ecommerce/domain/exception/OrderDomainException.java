package kh.gov.mptc.ecommerce.domain.exception;

import kh.mptc.gov.ecommerce.domian.exception.DomainException;

public class OrderDomainException extends DomainException {

    public OrderDomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderDomainException(String message) {
        super(message);
    }
}
