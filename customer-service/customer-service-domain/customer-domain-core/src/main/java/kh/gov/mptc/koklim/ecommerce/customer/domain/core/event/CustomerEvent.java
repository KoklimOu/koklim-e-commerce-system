package kh.gov.mptc.koklim.ecommerce.customer.domain.core.event;

import kh.gov.mptc.koklim.ecommerce.common.domain.event.DomainEvent;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.entity.Customer;

public abstract class CustomerEvent implements DomainEvent<Customer> {
    private final Customer customer;

    public CustomerEvent(Customer customer){
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

}
