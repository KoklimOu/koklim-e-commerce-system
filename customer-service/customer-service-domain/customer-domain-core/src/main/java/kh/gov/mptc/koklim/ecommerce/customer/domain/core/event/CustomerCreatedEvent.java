package kh.gov.mptc.koklim.ecommerce.customer.domain.core.event;

import kh.gov.mptc.koklim.ecommerce.customer.domain.core.entity.Customer;

import java.time.ZonedDateTime;

public class CustomerCreatedEvent extends CustomerEvent{
    private final ZonedDateTime createdAt;

    public CustomerCreatedEvent(Customer customer, ZonedDateTime createdAt){
        super(customer);
        this.createdAt = createdAt;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
