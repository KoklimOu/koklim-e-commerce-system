package kh.gov.mptc.koklim.ecommerce.order.application.port.output;

import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Order;
import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderResponse;

public interface OrderRepository {
    CreateOrderResponse saveOrder(Order order);
}
