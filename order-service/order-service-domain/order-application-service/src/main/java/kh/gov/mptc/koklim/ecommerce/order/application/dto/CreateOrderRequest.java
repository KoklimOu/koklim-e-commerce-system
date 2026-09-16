package kh.gov.mptc.koklim.ecommerce.order.application.dto;

import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.*;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.OrderItem;

import java.util.List;

public record CreateOrderRequest(
        CustomerId customerId,
        StreetAddress deliveryAddress,
        BusinessId businessId,
        Money price,
        List<OrderItem> items,
        List<String> failureMessages,
        TrackingId trackingId,
        OrderStatus orderStatus
) {

}
