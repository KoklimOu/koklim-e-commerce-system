package kh.gov.mptc.koklim.ecommerce.order.application.service.port.output;

import kh.gov.mptc.koklim.ecommerce.order.domaincore.entity.Order;
import kh.gov.mptc.koklim.ecommerce.order.application.service.dto.CreateOrderResponse;

public interface OrderRepository {
    CreateOrderResponse saveOrder(Order order);
}
