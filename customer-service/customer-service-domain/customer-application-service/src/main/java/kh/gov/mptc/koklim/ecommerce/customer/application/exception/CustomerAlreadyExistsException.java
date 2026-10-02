package kh.gov.mptc.koklim.ecommerce.customer.application.exception;

import kh.gov.mptc.koklim.ecommerce.customer.domain.core.exception.CustomerDomainException;

public class CustomerAlreadyExistsException extends CustomerDomainException {

    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
