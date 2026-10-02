package kh.gov.mptc.koklim.ecommerce.customer.domain.core.exception;

import kh.gov.mptc.koklim.ecommerce.common.domain.exception.DomainException;

public class CustomerDomainException  extends DomainException {
    public CustomerDomainException(String message) {
        super(message);
    }

    public CustomerDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
