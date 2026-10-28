package kh.gov.mptc.koklim.ecommerce.gateway.config;

import com.nimbusds.jwt.JWTParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    // Must match the registration id in application.yaml (spring.security.oauth2.client.registration.keycloak)
    private static final String LOGIN_URL = "/oauth2/authorization/keycloak";

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         ReactiveClientRegistrationRepository clientRegistrationRepository) {
        return http
                // TODO (next step): enable CSRF protection. The browser session is a cookie,
                // so state-changing requests (POST/PUT/DELETE) need it before real use.
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/actuator/health/**").permitAll()
                        .pathMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/orders/**").hasRole("CUSTOMER")
                        .pathMatchers("/api/v1/config/**").hasRole("ADMIN")
                        .anyExchange().authenticated())
                // Browser users: log in through Keycloak, session cookie, tokens stay on the gateway.
                .oauth2Login(Customizer.withDefaults())
                // API clients (Postman, services): Bearer JWT.
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtConverter())))
                // POST /logout: ends the gateway session and the Keycloak session, then returns to Vue.
                .logout(logout -> logout
                        .logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository)))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint()))
                .build();
    }

    /** fetch() calls to /api and /auth must get a 401, not a redirect to the Keycloak login page. */
    private ServerAuthenticationEntryPoint entryPoint() {
        DelegatingServerAuthenticationEntryPoint entryPoint = new DelegatingServerAuthenticationEntryPoint(
                new DelegatingServerAuthenticationEntryPoint.DelegateEntry(
                        ServerWebExchangeMatchers.pathMatchers("/api/**", "/auth/**"),
                        new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)));
        entryPoint.setDefaultEntryPoint(new RedirectServerAuthenticationEntryPoint(LOGIN_URL));
        return entryPoint;
    }

    /** RP-initiated logout: redirects the browser to Keycloak's end-session endpoint with id_token_hint. */
    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler(ReactiveClientRegistrationRepository repository) {
        OidcClientInitiatedServerLogoutSuccessHandler handler =
                new OidcClientInitiatedServerLogoutSuccessHandler(repository);
        // Must be listed under "Valid post logout redirect URIs" of the ecommerce-gateway client in Keycloak.
        handler.setPostLogoutRedirectUri("http://localhost:5173/");
        return handler;
    }

    /** Browser login: copy Keycloak realm roles (from the access token) into ROLE_* authorities. */
    @Bean
    public ReactiveOAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcReactiveOAuth2UserService delegate = new OidcReactiveOAuth2UserService();
        return request -> delegate.loadUser(request).map(oidcUser -> {
            Set<GrantedAuthority> authorities = new HashSet<>(oidcUser.getAuthorities());
            authorities.addAll(realmRoleAuthorities(request.getAccessToken().getTokenValue()));
            return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        });
    }

    // The token was just received from Keycloak over the back channel, so it is only read here, not validated.
    private static List<GrantedAuthority> realmRoleAuthorities(String accessToken) {
        List<GrantedAuthority> result = new ArrayList<>();
        try {
            Object realmAccess = JWTParser.parse(accessToken).getJWTClaimsSet().getClaim("realm_access");
            if (realmAccess instanceof Map<?, ?> map && map.get("roles") instanceof Collection<?> roles) {
                roles.forEach(role -> result.add(new SimpleGrantedAuthority("ROLE_" + role)));
            }
        } catch (java.text.ParseException e) {
            // not a JWT: no roles
        }
        return result;
    }

    /** Bearer JWT: same idea, reading realm_access.roles from the validated token. */
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> keycloakJwtConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null || !(realmAccess.get("roles") instanceof Collection<?> roles)) {
                return List.<GrantedAuthority>of();
            }
            return roles.stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();
        });
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}
