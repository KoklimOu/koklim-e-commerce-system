package kh.gov.mptc.koklim.ecommerce.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;

// user-service is an OAuth2 resource server: it never logs anybody in.
// The gateway does the Keycloak login (BFF) and forwards the access token (TokenRelay);
// here we only validate that token and read the roles from it.
@Configuration
@EnableWebSecurity
@EnableMethodSecurity   // lets us add @PreAuthorize("hasRole('ADMIN')") on single methods later
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Stateless API with Bearer tokens (no cookies), so CSRF does not apply here
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health/**").permitAll()
                        // Own profile: any logged-in user (read and edit)
                        .requestMatchers("/api/v1/users/me").authenticated()
                        // Public profile of one user: any logged-in user
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/*").authenticated()
                        // Admin client: manages identity in Keycloak (same ADMIN role as the gateway)
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        // Anything else is closed, so a forgotten endpoint is never open by accident
                        .requestMatchers("/api/v1/users/**").denyAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtConverter())))
                .build();
    }

    /** Keycloak puts realm roles in realm_access.roles; Spring needs them as ROLE_* authorities. */
    private JwtAuthenticationConverter keycloakJwtConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setPrincipalClaimName("preferred_username");   // authentication.getName() = Keycloak username
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null || !(realmAccess.get("roles") instanceof Collection<?> roles)) {
                return List.<GrantedAuthority>of();
            }
            return roles.stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();
        });
        return converter;
    }
}
