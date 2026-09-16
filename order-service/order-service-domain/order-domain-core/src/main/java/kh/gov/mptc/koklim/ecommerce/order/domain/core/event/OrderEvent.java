package kh.gov.mptc.koklim.ecommerce.order.domain.core.event;

import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Order;
import kh.gov.mptc.koklim.ecommerce.common.domain.event.DomainEvent;

import java.time.ZonedDateTime;

//abstract means:
//This class is incomplete by itself and is meant to be used as a base/parent class for other classes.

public abstract class OrderEvent implements DomainEvent<Order> {
    private final Order order;
    private final ZonedDateTime createdAt;

    public OrderEvent(Order order, ZonedDateTime createdAt) {
        this.order = order;
        this.createdAt = createdAt;
    }
    public Order getOrder() {
        return order;
    }
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
