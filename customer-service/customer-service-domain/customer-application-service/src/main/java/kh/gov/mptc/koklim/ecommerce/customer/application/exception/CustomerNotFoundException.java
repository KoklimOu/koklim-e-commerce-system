package kh.gov.mptc.koklim.ecommerce.customer.application.exception;

import kh.gov.mptc.koklim.ecommerce.customer.domain.core.exception.CustomerDomainException;

public class CustomerNotFoundException extends CustomerDomainException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
