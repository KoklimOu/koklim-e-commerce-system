package kh.gov.mptc.koklim.ecommerce.user.service;

import org.keycloak.representations.idm.UserRepresentation;

/**
 * Everything user-service asks Keycloak to do. Keycloak owns the identity
 * (username, email, names, password, roles); we only keep the profile.
 */
public interface KeycloakUserService {

    /** Creates the user in Keycloak. Throws KeycloakException (with Keycloak's status) if it fails. */
    void createKeycloakUser(UserRepresentation userRepresentation);

    /** Finds a Keycloak user by exact username. Throws KeycloakException (404) if there is none. */
    UserRepresentation getKeycloakUserByUsername(String username);

    /** Deletes a user from Keycloak. Never throws: used to roll back, so it only logs on failure. */
    void deleteKeycloakUserQuietly(String keycloakUserId);
}
