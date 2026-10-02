package kh.gov.mptc.koklim.ecommerce.customer.domain.core.service;

import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.Email;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.PhoneNumber;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.entity.Customer;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.event.CustomerCreatedEvent;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.event.CustomerDeactivatedEvent;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.event.CustomerUpdatedEvent;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CustomerDomainServiceImpl implements CustomerDomainService{

    @Override
    public CustomerCreatedEvent validateAndInitiateCustomer(Customer customer) {
        customer.validateCustomer();
        customer.initiateCustomer();
        return new CustomerCreatedEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public CustomerUpdatedEvent updateCustomer(Customer customer, String familyName, String givenName,
                                               Email email, PhoneNumber phoneNumber) {
        customer.updateCustomer(familyName, givenName, email, phoneNumber);
        return new CustomerUpdatedEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public CustomerDeactivatedEvent deactivateCustomer(Customer customer) {
        customer.deactivateCustomer();
        return new CustomerDeactivatedEvent(customer.getId(), ZonedDateTime.now(ZoneId.of("UTC")));
    }

}
