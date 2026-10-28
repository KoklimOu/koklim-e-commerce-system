package kh.gov.mptc.koklim.ecommerce.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

// Admin creates a user in KEYCLOAK. The public profile is created later, at the user's first /me.
@Builder
public record AdminCreateUserRequest(
        @NotBlank @Size(min = 3, max = 50)
        String username,
        @NotBlank @Email @Size(max = 150)
        String email,
        @NotBlank @Size(max = 100)
        String firstName,
        @NotBlank @Size(max = 100)
        String lastName,
        @NotBlank @Size(min = 8, max = 100)
        String temporaryPassword,       // user must change it at first login
        List<@NotBlank String> roles    // Keycloak realm roles; empty = CUSTOMER
) {
}
