package kh.gov.mptc.koklim.ecommerce.user.config;

import kh.gov.mptc.koklim.ecommerce.user.exception.KeycloakAdminException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;

// Calls the Keycloak Admin REST API as a service account (client_credentials).
// Keycloak owns identity (username, email, password, roles, enabled); this class is the only place that writes it.
// Keycloak answers are read as Maps on purpose: we only take the few fields we need.
@Component
public class KeycloakAdminClient {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP = new ParameterizedTypeReference<>() {};
    private static final ParameterizedTypeReference<List<Map<String, Object>>> MAP_LIST = new ParameterizedTypeReference<>() {};

    private final KeycloakAdminProperties props;
    private final RestClient rest = RestClient.builder().build();

    private String accessToken;
    private Instant accessTokenExpiresAt = Instant.EPOCH;

    public KeycloakAdminClient(KeycloakAdminProperties props) {
        this.props = props;
    }

    // ---------------------------------------------------------------- users

    /** Creates the user (with roles) and returns its Keycloak id. Fails before creating if a role does not exist. */
    public UUID createUser(NewKeycloakUser user, Collection<String> roles) {
        return execute(() -> {
            List<Map<String, Object>> roleReps = resolveRoles(roles);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("username", user.username());
            body.put("email", user.email());
            body.put("firstName", user.firstName());
            body.put("lastName", user.lastName());
            body.put("enabled", true);
            body.put("emailVerified", true);
            body.put("credentials", List.of(Map.of(
                    "type", "password", "value", user.password(), "temporary", user.temporaryPassword())));

            ResponseEntity<Void> response = rest.post().uri(adminUrl("/users"))
                    .headers(h -> h.setBearerAuth(token()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve().toBodilessEntity();

            URI location = response.getHeaders().getLocation();
            if (location == null) {
                throw new KeycloakAdminException(HttpStatus.BAD_GATEWAY, "Keycloak created the user but returned no id");
            }
            UUID id = UUID.fromString(location.getPath().substring(location.getPath().lastIndexOf('/') + 1));

            try {
                mapRoles(HttpMethod.POST, id, roleReps);
            } catch (RuntimeException e) {
                deleteQuietly(id);   // do not leave a half-created user behind
                throw e;
            }
            return id;
        });
    }

    public KeycloakUser getUser(UUID id) {
        return execute(() -> toUser(getUserRepresentation(id)));
    }

    public List<KeycloakUser> searchUsers(String search, int first, int max) {
        return execute(() -> {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(adminUrl("/users"))
                    .queryParam("first", first).queryParam("max", max);
            if (search != null && !search.isBlank()) uri.queryParam("search", search.trim());

            List<Map<String, Object>> users = rest.get().uri(uri.build().encode().toUri())
                    .headers(h -> h.setBearerAuth(token()))
                    .retrieve().body(MAP_LIST);
            return users == null ? List.<KeycloakUser>of() : users.stream().map(KeycloakAdminClient::toUser).toList();
        });
    }

    public long countUsers(String search) {
        return execute(() -> {
            UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(adminUrl("/users/count"));
            if (search != null && !search.isBlank()) uri.queryParam("search", search.trim());

            Long count = rest.get().uri(uri.build().encode().toUri())
                    .headers(h -> h.setBearerAuth(token()))
                    .retrieve().body(Long.class);
            return count == null ? 0L : count;
        });
    }

    public void setEnabled(UUID id, boolean enabled) {
        execute(() -> {
            // Send the whole user back: with Keycloak's user profile a partial PUT can fail on required fields
            Map<String, Object> user = getUserRepresentation(id);
            user.put("enabled", enabled);
            rest.put().uri(adminUrl("/users/{id}"), id)
                    .headers(h -> h.setBearerAuth(token()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(user)
                    .retrieve().toBodilessEntity();
            return null;
        });
    }

    public void resetPassword(UUID id, String password, boolean temporary) {
        execute(() -> {
            rest.put().uri(adminUrl("/users/{id}/reset-password"), id)
                    .headers(h -> h.setBearerAuth(token()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("type", "password", "value", password, "temporary", temporary))
                    .retrieve().toBodilessEntity();
            return null;
        });
    }

    public void deleteUser(UUID id) {
        execute(() -> {
            rest.delete().uri(adminUrl("/users/{id}"), id)
                    .headers(h -> h.setBearerAuth(token()))
                    .retrieve().toBodilessEntity();
            return null;
        });
    }

    // ---------------------------------------------------------------- roles (realm roles)

    /** Realm roles assigned directly to the user (Keycloak's own default-roles-* entry is hidden). */
    public List<String> getRealmRoles(UUID id) {
        return execute(() -> {
            List<Map<String, Object>> roles = rest.get().uri(adminUrl("/users/{id}/role-mappings/realm"), id)
                    .headers(h -> h.setBearerAuth(token()))
                    .retrieve().body(MAP_LIST);
            return roles == null ? List.<String>of() : roles.stream()
                    .map(r -> String.valueOf(r.get("name")))
                    .filter(name -> !name.startsWith("default-roles-"))
                    .sorted()
                    .toList();
        });
    }

    public void addRealmRoles(UUID id, Collection<String> roles) {
        execute(() -> {
            mapRoles(HttpMethod.POST, id, resolveRoles(roles));
            return null;
        });
    }

    public void removeRealmRoles(UUID id, Collection<String> roles) {
        execute(() -> {
            mapRoles(HttpMethod.DELETE, id, resolveRoles(roles));
            return null;
        });
    }

    // ---------------------------------------------------------------- internals

    private Map<String, Object> getUserRepresentation(UUID id) {
        Map<String, Object> user = rest.get().uri(adminUrl("/users/{id}"), id)
                .headers(h -> h.setBearerAuth(token()))
                .retrieve().body(MAP);
        if (user == null) {
            throw new KeycloakAdminException(HttpStatus.NOT_FOUND, "User not found in Keycloak");
        }
        return new LinkedHashMap<>(user);
    }

    private List<Map<String, Object>> resolveRoles(Collection<String> names) {
        List<Map<String, Object>> reps = new ArrayList<>();
        for (String name : names) {
            try {
                Map<String, Object> rep = rest.get().uri(adminUrl("/roles/{name}"), name)
                        .headers(h -> h.setBearerAuth(token()))
                        .retrieve().body(MAP);
                reps.add(rep);
            } catch (HttpClientErrorException.NotFound e) {
                throw new KeycloakAdminException(HttpStatus.BAD_REQUEST, "Role '" + name + "' does not exist in Keycloak");
            }
        }
        return reps;
    }

    private void mapRoles(HttpMethod method, UUID userId, List<Map<String, Object>> roleReps) {
        if (roleReps.isEmpty()) return;
        rest.method(method).uri(adminUrl("/users/{id}/role-mappings/realm"), userId)
                .headers(h -> h.setBearerAuth(token()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(roleReps)
                .retrieve().toBodilessEntity();
    }

    private void deleteQuietly(UUID id) {
        try {
            rest.delete().uri(adminUrl("/users/{id}"), id)
                    .headers(h -> h.setBearerAuth(token()))
                    .retrieve().toBodilessEntity();
        } catch (RuntimeException ignored) {
            // best effort; the original error is what the caller needs to see
        }
    }

    private static KeycloakUser toUser(Map<String, Object> m) {
        return new KeycloakUser(
                UUID.fromString(String.valueOf(m.get("id"))),
                (String) m.get("username"),
                (String) m.get("email"),
                (String) m.get("firstName"),
                (String) m.get("lastName"),
                Boolean.TRUE.equals(m.get("enabled")));
    }

    private String adminUrl(String path) {
        return props.serverUrl() + "/admin/realms/" + props.realm() + path;
    }

    // Service-account token, cached until shortly before it expires
    private synchronized String token() {
        if (accessToken != null && Instant.now().isBefore(accessTokenExpiresAt.minusSeconds(30))) {
            return accessToken;
        }
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", props.clientId());
        form.add("client_secret", props.clientSecret());

        Map<String, Object> response;
        try {
            response = rest.post()
                    .uri(props.serverUrl() + "/realms/" + props.realm() + "/protocol/openid-connect/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve().body(MAP);
        } catch (RestClientException e) {
            throw new KeycloakAdminException(HttpStatus.BAD_GATEWAY,
                    "Cannot get a Keycloak admin token. Check keycloak.admin.* settings and that Keycloak is running");
        }
        if (response == null || response.get("access_token") == null) {
            throw new KeycloakAdminException(HttpStatus.BAD_GATEWAY, "Keycloak returned no admin token");
        }
        accessToken = (String) response.get("access_token");
        long expiresIn = response.get("expires_in") instanceof Number n ? n.longValue() : 60;
        accessTokenExpiresAt = Instant.now().plusSeconds(expiresIn);
        return accessToken;
    }

    // Turns Keycloak/HTTP failures into KeycloakAdminException with the status WE want to answer with
    private <T> T execute(Supplier<T> call) {
        try {
            return call.get();
        } catch (KeycloakAdminException e) {
            throw e;
        } catch (HttpClientErrorException e) {
            int code = e.getStatusCode().value();
            throw switch (code) {
                case 404 -> new KeycloakAdminException(HttpStatus.NOT_FOUND, "Not found in Keycloak");
                case 409 -> new KeycloakAdminException(HttpStatus.CONFLICT,
                        "A Keycloak user with this username or email already exists");
                case 400 -> new KeycloakAdminException(HttpStatus.BAD_REQUEST,
                        "Keycloak rejected the request: " + e.getResponseBodyAsString());
                case 401, 403 -> new KeycloakAdminException(HttpStatus.BAD_GATEWAY,
                        "The Keycloak admin client is not allowed to do this. Check its service account roles");
                default -> new KeycloakAdminException(HttpStatus.BAD_GATEWAY, "Keycloak request failed (" + code + ")");
            };
        } catch (RestClientException e) {
            throw new KeycloakAdminException(HttpStatus.BAD_GATEWAY, "Keycloak is not reachable or failed");
        }
    }
}
