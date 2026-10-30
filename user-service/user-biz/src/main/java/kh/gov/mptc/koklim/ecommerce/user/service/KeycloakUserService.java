package kh.gov.mptc.koklim.ecommerce.user.service;

import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

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

    void updateKeycloakUser(String id, UserRepresentation userRepresentation);

    UserRepresentation getKeycloakUserById(String id);

    /** Lists the Keycloak users of the realm (first 100, Keycloak's default page size). */
    List<UserRepresentation> getKeycloakUsers();

    /** Deletes a user from Keycloak. Throws KeycloakException if it fails. */
    void deleteKeycloakUser(String keycloakUserId);
}
