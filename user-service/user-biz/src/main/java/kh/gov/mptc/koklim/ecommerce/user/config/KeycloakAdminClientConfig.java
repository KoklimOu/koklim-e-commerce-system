package kh.gov.mptc.koklim.ecommerce.user.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// The admin client logs in to Keycloak as a service account (client credentials).
// Values come from keycloak.admin.* in application.yaml; the secret comes from an environment variable.
@Configuration
public class KeycloakAdminClientConfig {

    public static final String REALM_NAME = "ecommerce";

    @Bean
    public Keycloak keycloak(@Value("${keycloak.admin.server-url}") String serverUrl,
                             @Value("${keycloak.admin.realm}") String realm,
                             @Value("${keycloak.admin.client-id}") String clientId,
                             @Value("${keycloak.admin.client-secret}") String clientSecret) {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .realm(realm)
                .build();
    }
}
