package kh.gov.mptc.koklim.ecommerce.user.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

// The only things a user can edit here. Email, name, password and roles are changed in Keycloak.
@Builder
public record ProfileUpdateRequest(
        @Size(max = 100)
        String displayName,
        @Size(max = 500)
        @Pattern(regexp = "^https?://.+$", message = "must start with http:// or https://")
        String avatarUrl,
        @Size(max = 500)
        String bio
) {
}
