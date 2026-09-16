package kh.gov.mptc.koklim.ecommerce.order.restapi.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderCreateResponse(
        UUID orderId
) {
}
