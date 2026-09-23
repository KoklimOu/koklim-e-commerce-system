package kh.gov.mptc.koklim.ecommerce.order.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommandOrderAddress(
        String street,
        String postalCode,
        String city
) {
}
