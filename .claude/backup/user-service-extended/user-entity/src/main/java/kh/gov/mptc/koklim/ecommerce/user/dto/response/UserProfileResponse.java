package kh.gov.mptc.koklim.ecommerce.user.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

// Public view: safe to show to any logged-in user.
@Builder
public record UserProfileResponse(
        UUID id,
        String username,
        String displayName,
        String avatarUrl,
        String bio,
        Instant createdAt
) {
}
