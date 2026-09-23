package kh.gov.mptc.koklim.ecommerce.order.persistence.adapter;

import kh.gov.mptc.koklim.ecommerce.order.application.port.output.BusinessRepository;
import kh.gov.mptc.koklim.ecommerce.order.application.port.output.CustomerRepository;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Order;
import kh.gov.mptc.koklim.ecommerce.order.persistence.repository.OrderJpaRepository;
import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderResult;
import kh.gov.mptc.koklim.ecommerce.order.application.port.output.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/*A secondary adapter's only job is to save orders to the database. Business rules belong in the core.*/

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public CreateOrderResult saveOrder(Order order) {
        // Map OrderEntity to Order
        return null;
    }
}
