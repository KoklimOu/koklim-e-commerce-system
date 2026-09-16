package kh.gov.mptc.koklim.ecommerce.order.restapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderItemRequest(
        @NotNull
        UUID productId,
        @NotNull
        Integer quantity,
        @NotNull
        BigDecimal subtotal,
        @NotNull
        BigDecimal price
) {
}
