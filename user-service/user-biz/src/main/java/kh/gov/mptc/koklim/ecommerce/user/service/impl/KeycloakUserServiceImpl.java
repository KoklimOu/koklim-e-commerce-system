package kh.gov.mptc.koklim.ecommerce.user.service.impl;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import kh.gov.mptc.koklim.ecommerce.user.config.KeycloakAdminClientConfig;
import kh.gov.mptc.koklim.ecommerce.user.exception.KeycloakException;
import kh.gov.mptc.koklim.ecommerce.user.service.KeycloakUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserServiceImpl implements KeycloakUserService {

    private final Keycloak keycloak;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Override
    public void createKeycloakUser(UserRepresentation userRepresentation) {
        try (Response response = keycloak.realm(realm).users().create(userRepresentation)) {
            if (response.getStatus() != HttpStatus.CREATED.value()) {
                log.error("Cannot create user in Keycloak, status {}", response.getStatus());
                throw new KeycloakException(
                        HttpStatus.valueOf(response.getStatus()),
                        "Create user in keycloak failed with status " + response.getStatus());
            }
        } catch (ProcessingException | WebApplicationException e) {
            // Keycloak is down, or the admin client secret / roles are wrong
            log.error("Keycloak admin client failed", e);
            throw new KeycloakException(HttpStatus.BAD_GATEWAY,
                    "Cannot reach Keycloak (" + e.getClass().getSimpleName() + ": " + e.getMessage()
                            + "). Check keycloak.admin.* settings and the admin client roles");
        }
    }

    @Override
    public UserRepresentation getKeycloakUserByUsername(String username) {
        return keycloak.realm(realm)
                .users()
                .search(username, true)
                .stream()
                .findFirst()
                .orElseThrow(() -> new KeycloakException(
                        HttpStatus.NOT_FOUND,
                        "User in Keycloak not found"));
    }

    @Override
    public void deleteKeycloakUserQuietly(String keycloakUserId) {
        try (Response ignored = keycloak.realm(realm).users().delete(keycloakUserId)) {
            log.warn("Rolled back Keycloak user {}", keycloakUserId);
        } catch (RuntimeException e) {
            log.error("Could not roll back Keycloak user {}: delete it by hand", keycloakUserId, e);
        }
    }

    @Override
    public void updateKeycloakUser(String id, UserRepresentation userRepresentation) {
        try {
            keycloak.realm(KeycloakAdminClientConfig.REALM_NAME)
                    .users()
                    .get(id)
                    .update(userRepresentation);
        }catch (Exception e){
            log.error("Could not update Keycloak user {}", id, e);
        }
    }

    @Override
    public UserRepresentation getKeycloakUserById(String id) {
        try {
            return keycloak.realm(KeycloakAdminClientConfig.REALM_NAME).users().get(id).toRepresentation();
        } catch (Exception e) {
            log.error("Could not get Keycloak user {}", id, e);
        }
        return null;
    }

    @Override
    public List<UserRepresentation> getKeycloakUsers() {
        return keycloak.realm(realm).users().list();
    }

    @Override
    public void deleteKeycloakUser(String keycloakUserId) {
        try (Response response = keycloak.realm(realm).users().delete(keycloakUserId)) {
            if (response.getStatus() != HttpStatus.NO_CONTENT.value()) {
                log.error("Cannot delete user {} in Keycloak, status {}", keycloakUserId, response.getStatus());
                throw new KeycloakException(
                        HttpStatus.valueOf(response.getStatus()),
                        "Delete user in keycloak failed with status " + response.getStatus());
            }
        } catch (ProcessingException | WebApplicationException e) {
            log.error("Keycloak admin client failed", e);
            throw new KeycloakException(HttpStatus.BAD_GATEWAY,
                    "Cannot reach Keycloak (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ")");
        }
    }
}
