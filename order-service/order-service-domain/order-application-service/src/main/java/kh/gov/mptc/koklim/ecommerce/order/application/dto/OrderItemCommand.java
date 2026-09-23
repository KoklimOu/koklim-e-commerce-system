package kh.gov.mptc.koklim.ecommerce.order.application.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemCommand(
        UUID productId,
        Integer quantity,
        BigDecimal subtotal,
        BigDecimal price
) {
}
