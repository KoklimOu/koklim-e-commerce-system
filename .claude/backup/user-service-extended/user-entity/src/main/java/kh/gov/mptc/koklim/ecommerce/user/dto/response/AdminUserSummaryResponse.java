package kh.gov.mptc.koklim.ecommerce.user.dto.response;

import lombok.Builder;

import java.util.UUID;

// One row of the admin user list (identity data from Keycloak)
@Builder
public record AdminUserSummaryResponse(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        boolean enabled
) {
}
