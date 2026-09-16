package kh.gov.mptc.koklim.ecommerce.order.domain.core.event;

import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Order;

import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent{
    public OrderPaidEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
