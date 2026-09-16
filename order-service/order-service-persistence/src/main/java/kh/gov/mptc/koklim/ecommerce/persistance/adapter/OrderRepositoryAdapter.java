package kh.gov.mptc.koklim.ecommerce.persistance.adapter;

import kh.gov.mptc.koklim.ecommerce.order.domaincore.entity.Order;
import kh.gov.mptc.koklim.ecommerce.persistance.repository.OrderJpaRepository;
import kh.gov.mptc.koklim.ecommerce.order.application.service.dto.CreateOrderResponse;
import kh.gov.mptc.koklim.ecommerce.order.application.service.port.output.OrderRepository;



public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public CreateOrderResponse saveOrder(Order order) {
        // Map Order to OrderEntity
        // Map OrderEntity to Order
        return null;
    }
}
