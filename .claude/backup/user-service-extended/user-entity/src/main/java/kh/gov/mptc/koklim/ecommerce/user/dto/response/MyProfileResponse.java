package kh.gov.mptc.koklim.ecommerce.user.dto.response;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

// What /me returns: the stored public profile + identity data read live from the Keycloak token.
@Builder
public record MyProfileResponse(
        UUID id,
        String username,
        String displayName,
        String avatarUrl,
        String bio,
        String email,          // from Keycloak
        String fullName,       // from Keycloak
        List<String> roles     // from Keycloak
) {
}
