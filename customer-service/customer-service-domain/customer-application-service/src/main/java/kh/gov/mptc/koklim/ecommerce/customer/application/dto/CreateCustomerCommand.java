package kh.gov.mptc.koklim.ecommerce.customer.application.dto;

public record CreateCustomerCommand(
        String username,
        String familyName,
        String givenName,
        String email,
        String phoneNumber
) {
}
