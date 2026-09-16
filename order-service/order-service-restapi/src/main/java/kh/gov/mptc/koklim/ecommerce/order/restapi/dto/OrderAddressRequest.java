package kh.gov.mptc.koklim.ecommerce.order.restapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderAddressRequest(
        @Size(max = 255)
        String street,
        @NotNull
        @Size(max = 10)
        String postalCode,
        @NotNull
        @Size(max = 255)
        String city
) {
}
