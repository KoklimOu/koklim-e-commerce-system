package kh.gov.mptc.koklim.ecommerce.order.domaincore.exception;

import kh.gov.mptc.koklim.ecommerce.commondomain.exception.DomainException;

public class OrderDomainException extends DomainException {

    public OrderDomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderDomainException(String message) {
        super(message);
    }
}
