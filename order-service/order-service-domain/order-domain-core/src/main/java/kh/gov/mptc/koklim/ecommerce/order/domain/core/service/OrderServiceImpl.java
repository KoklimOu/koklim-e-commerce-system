package kh.gov.mptc.koklim.ecommerce.order.domain.core.service;

import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Business;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Order;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.event.OrderCancelledEvent;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.event.OrderCreatedEvent;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.event.OrderPaidEvent;

import java.util.List;

public class OrderServiceImpl implements OrderDomainService {
    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Business business) {
        return null;
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        return null;
    }

    @Override
    public void approveOrder(Order order) {

    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        return null;
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {

    }
}
