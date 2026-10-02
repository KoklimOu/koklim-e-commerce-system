package kh.gov.mptc.koklim.ecommerce.customer.application.dto;

import java.util.UUID;

public record DeactivateCustomerCommand(
        UUID customerId
) {
}
