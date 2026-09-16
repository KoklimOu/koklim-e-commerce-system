package kh.gov.mptc.koklim.ecommerce.order.domaincore.event;

//serialization

import kh.gov.mptc.koklim.ecommerce.order.domaincore.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {
    public OrderCreatedEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
    // Order information
}
