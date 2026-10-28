package kh.gov.mptc.koklim.ecommerce.user.config;

import java.util.UUID;

// The part of a Keycloak user this service cares about
public record KeycloakUser(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        boolean enabled
) {
}
