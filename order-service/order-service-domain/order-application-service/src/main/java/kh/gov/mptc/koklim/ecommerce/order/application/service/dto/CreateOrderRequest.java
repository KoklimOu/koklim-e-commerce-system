package kh.gov.mptc.koklim.ecommerce.order.application.service.dto;

import kh.gov.mptc.koklim.ecommerce.commondomain.valueobject.*;
import kh.gov.mptc.koklim.ecommerce.order.domaincore.entity.OrderItem;

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
