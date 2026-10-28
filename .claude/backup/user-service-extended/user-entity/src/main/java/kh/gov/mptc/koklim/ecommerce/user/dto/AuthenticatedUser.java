package kh.gov.mptc.koklim.ecommerce.user.dto;

import java.util.List;
import java.util.UUID;

// Who is calling, read from the Keycloak token by the controller layer.
// A plain record, so the service layer does not depend on Spring Security.
public record AuthenticatedUser(
        UUID id,              // token "sub" = Keycloak user id
        String username,      // preferred_username
        String email,
        String fullName,      // "name" claim
        List<String> roles    // realm roles without the ROLE_ prefix
) {
}
