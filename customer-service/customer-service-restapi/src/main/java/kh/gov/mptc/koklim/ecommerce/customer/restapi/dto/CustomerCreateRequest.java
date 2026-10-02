package kh.gov.mptc.koklim.ecommerce.customer.restapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CustomerCreateRequest(
        @NotBlank
        String username,
        String familyName,
        String givenName,
        @NotBlank
        @Email
        String email,
        String phoneNumber
) {
}
