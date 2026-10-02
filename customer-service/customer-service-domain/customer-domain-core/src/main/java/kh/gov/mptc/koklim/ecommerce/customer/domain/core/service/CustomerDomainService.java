package kh.gov.mptc.koklim.ecommerce.customer.domain.core.service;

import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.Email;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.PhoneNumber;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.entity.Customer;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.event.CustomerCreatedEvent;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.event.CustomerDeactivatedEvent;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.event.CustomerUpdatedEvent;


public interface CustomerDomainService {
    CustomerCreatedEvent validateAndInitiateCustomer(Customer customer);

    CustomerUpdatedEvent updateCustomer(Customer customer, String familyName, String givenName,
                                        Email email, PhoneNumber phoneNumber);

    CustomerDeactivatedEvent deactivateCustomer(Customer customer);
}
