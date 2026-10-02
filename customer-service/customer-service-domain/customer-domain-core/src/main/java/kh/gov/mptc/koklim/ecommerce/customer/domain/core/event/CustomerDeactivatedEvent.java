package kh.gov.mptc.koklim.ecommerce.customer.domain.core.event;

import kh.gov.mptc.koklim.ecommerce.common.domain.event.DomainEvent;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.CustomerId;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.entity.Customer;

import java.time.ZonedDateTime;

public class CustomerDeactivatedEvent implements DomainEvent<Customer> {
    private final CustomerId customerId;
    private final ZonedDateTime deactivatedAt;

    public CustomerDeactivatedEvent(CustomerId customerId, ZonedDateTime deactivatedAt){
        this.customerId = customerId;
        this.deactivatedAt = deactivatedAt;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public ZonedDateTime getDeactivatedAt() {
        return deactivatedAt;
    }
}
