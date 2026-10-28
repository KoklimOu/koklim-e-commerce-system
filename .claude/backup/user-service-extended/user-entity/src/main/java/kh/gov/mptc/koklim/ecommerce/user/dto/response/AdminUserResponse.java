package kh.gov.mptc.koklim.ecommerce.user.dto.response;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

// Admin detail view: identity from Keycloak + public profile from our database.
// displayName/avatarUrl/bio are null until the user opens the app once (profile not created yet).
@Builder
public record AdminUserResponse(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        boolean enabled,
        List<String> roles,
        String displayName,
        String avatarUrl,
        String bio
) {
}
