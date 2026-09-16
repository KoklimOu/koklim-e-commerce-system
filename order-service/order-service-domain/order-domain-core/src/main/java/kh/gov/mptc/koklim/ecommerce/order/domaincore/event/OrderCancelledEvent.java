package kh.gov.mptc.koklim.ecommerce.order.domaincore.event;

import kh.gov.mptc.koklim.ecommerce.order.domaincore.entity.Order;

import java.time.ZonedDateTime;

public class OrderCancelledEvent extends OrderEvent{
    public OrderCancelledEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
