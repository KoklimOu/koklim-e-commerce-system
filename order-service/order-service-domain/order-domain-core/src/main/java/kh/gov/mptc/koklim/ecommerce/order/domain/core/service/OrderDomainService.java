package kh.gov.mptc.koklim.ecommerce.order.domain.core.service;

import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Business;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Order;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.event.OrderCancelledEvent;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.event.OrderCreatedEvent;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {
    OrderCreatedEvent validateAndInitiateOrder(Order order, Business business);

    OrderPaidEvent payOrder(Order order);

    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

    void cancelOrder(Order order, List<String> failureMessages);
}
