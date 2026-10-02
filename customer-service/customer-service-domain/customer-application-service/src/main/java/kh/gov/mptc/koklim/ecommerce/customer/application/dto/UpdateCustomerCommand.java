package kh.gov.mptc.koklim.ecommerce.customer.application.dto;

import java.util.UUID;

public record UpdateCustomerCommand(
        UUID customerId,
        String familyName,
        String givenName,
        String email,
        String phoneNumber
) {
}
