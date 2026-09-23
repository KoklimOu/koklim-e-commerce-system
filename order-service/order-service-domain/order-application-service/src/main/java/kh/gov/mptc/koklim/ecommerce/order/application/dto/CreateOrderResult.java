package kh.gov.mptc.koklim.ecommerce.order.application.dto;

import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Order;

import java.util.UUID;

public record CreateOrderResult(UUID orderId) {
}
