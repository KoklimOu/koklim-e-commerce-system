package kh.gov.mptc.koklim.ecommerce.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// keycloak.admin.* in application.yaml. The client is a Keycloak "service account" client
// (see docs/keycloak-notes.md); never put the real secret in git, use KEYCLOAK_ADMIN_CLIENT_SECRET.
@ConfigurationProperties(prefix = "keycloak.admin")
public record KeycloakAdminProperties(
        String serverUrl,      // e.g. http://localhost:8180
        String realm,          // e.g. ecommerce
        String clientId,       // e.g. user-service-admin
        String clientSecret
) {
}
