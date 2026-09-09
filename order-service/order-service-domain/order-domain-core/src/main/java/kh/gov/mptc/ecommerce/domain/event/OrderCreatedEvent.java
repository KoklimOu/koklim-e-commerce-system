package kh.gov.mptc.ecommerce.domain.event;

//serialization

import kh.gov.mptc.ecommerce.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {
    public OrderCreatedEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
    // Order information
}
