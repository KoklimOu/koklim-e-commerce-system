package kh.gov.mptc.koklim.ecommerce.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Used by the Vue router guard: 200 + JSON when the browser has a logged-in session,
 * 401 otherwise (see the entry point in SecurityConfig).
 */
@RestController
public class AuthController {

    @GetMapping("/auth/me")
    public Map<String, Object> me(@AuthenticationPrincipal OidcUser user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring("ROLE_".length()))
                .sorted()
                .toList();

        // LinkedHashMap, not Map.of: a missing claim (null) must not crash the endpoint.
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("username", user.getPreferredUsername());
        body.put("name", user.getFullName());
        body.put("email", user.getEmail());
        body.put("roles", roles);
        body.put("permissions", roles); // the Vue router guard reads `permissions`
        return body;
    }
}
