package kh.gov.mptc.koklim.ecommerce.order.domain.core.event;

//serialization

import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {
    public OrderCreatedEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
    // Order information
}
