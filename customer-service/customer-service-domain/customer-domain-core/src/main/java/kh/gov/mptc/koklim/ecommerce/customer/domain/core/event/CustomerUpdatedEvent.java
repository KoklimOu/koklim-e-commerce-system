package kh.gov.mptc.koklim.ecommerce.customer.domain.core.event;

import kh.gov.mptc.koklim.ecommerce.customer.domain.core.entity.Customer;

import java.time.ZonedDateTime;

public class CustomerUpdatedEvent extends CustomerEvent{
    private final ZonedDateTime updatedAt;

    public CustomerUpdatedEvent(Customer customer, ZonedDateTime updatedAt){
        super(customer);
        this.updatedAt = updatedAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }
}
