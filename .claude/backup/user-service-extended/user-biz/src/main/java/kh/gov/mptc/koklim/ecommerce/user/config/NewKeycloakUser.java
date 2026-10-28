package kh.gov.mptc.koklim.ecommerce.user.config;

public record NewKeycloakUser(
        String username,
        String email,
        String firstName,
        String lastName,
        String password,
        boolean temporaryPassword   // true = user must change it at first login
) {
}
