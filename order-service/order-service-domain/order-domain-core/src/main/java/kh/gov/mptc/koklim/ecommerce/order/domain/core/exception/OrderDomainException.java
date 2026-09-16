package kh.gov.mptc.koklim.ecommerce.order.domain.core.exception;

import kh.gov.mptc.koklim.ecommerce.common.domain.exception.DomainException;

public class OrderDomainException extends DomainException {

    public OrderDomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderDomainException(String message) {
        super(message);
    }
}
