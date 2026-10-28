# Keycloak and user-service

## Who owns what

| Data | Lives in | Why |
|---|---|---|
| username, email, first/last name, password, MFA, roles, enabled/disabled | **Keycloak** | identity and security: one source of truth |
| displayName, avatarUrl, bio (public profile) | **user-service database** (`user_profiles`) | app data Keycloak should not carry |

The profile row id **is** the Keycloak user id (`sub` in the token). The row is created automatically
the first time a user calls `GET /api/v1/users/me`. `username` is a copy that is refreshed on every `/me`.

## Endpoints (through the gateway, port 8585)

| Endpoint | Who | Does |
|---|---|---|
| `GET /api/v1/users/me` | any logged-in user | profile + email/name/roles from the token |
| `PUT /api/v1/users/me` | any logged-in user | edit displayName, avatarUrl, bio |
| `GET /api/v1/users/{id}` | any logged-in user | public profile of one user |
| `POST /api/v1/admin/users` | ADMIN | create user in Keycloak (temporary password, roles) |
| `GET /api/v1/admin/users?search=&page=&size=` | ADMIN | list/search users (from Keycloak) |
| `GET /api/v1/admin/users/{id}` | ADMIN | Keycloak identity + roles + profile |
| `PATCH /api/v1/admin/users/{id}/enabled` | ADMIN | enable / disable the account |
| `PUT /api/v1/admin/users/{id}/roles` | ADMIN | set realm roles (exact list) |
| `POST /api/v1/admin/users/{id}/reset-password` | ADMIN | set a new password |
| `DELETE /api/v1/admin/users/{id}` | ADMIN | delete Keycloak account and profile |
| `GET /actuator/health` | public | |

Roles come from `realm_access.roles` in the token (`ADMIN`, `CUSTOMER`, ...).

## Admin client setup (one time)

user-service calls the Keycloak Admin REST API with its own **service account**, not with the admin's token.
Run the script (Keycloak must be running; it is safe to run again):

    bash user-service/docs/setup-keycloak-admin-client.sh

It creates realm roles `ADMIN` + `CUSTOMER`, the client `user-service-admin` (confidential, service account on,
no browser login), gives the service account `manage-users`, `view-users`, `query-users`, `view-realm`
from `realm-management`, and prints the secret. Defaults: Keycloak `http://localhost:8180`, realm `ecommerce`,
master admin `admin` / `admin`. Override with `KEYCLOAK_URL`, `KC_REALM`, `KC_ADMIN_USER`, `KC_ADMIN_PASSWORD`.

Then start user-service with the printed secret (never commit it):

    KEYCLOAK_ADMIN_CLIENT_SECRET=<secret>

IntelliJ: Run > Edit Configurations > UserServiceApplication > Environment variables.
Verify everything, including the admin client:

    KEYCLOAK_ADMIN_CLIENT_SECRET=<secret> bash user-service/docs/check-env.sh

Prefer clicking? Same steps by hand: Clients > Create client `user-service-admin` > Client authentication ON,
only "Service accounts roles" ticked > Service accounts roles tab > Assign role > filter by clients >
`realm-management` > the four roles above > Credentials tab for the secret.

Other settings (application.yaml): `keycloak.admin.server-url` (`KEYCLOAK_URL`), `realm`, `client-id`, `client-secret`.

If the admin client is wrong the admin API answers **502 Bad Gateway** with a message saying what to check
(secret, service account roles, or Keycloak not running). An unknown role name answers 400.

## Token checks

- The token issuer must equal `spring.security.oauth2.resourceserver.jwt.issuer-uri`
  (default `http://localhost:8180/realms/ecommerce`, same as the gateway).
- The audience is not checked (same as the gateway) - tighten this before production.

Quick test:
    curl -H "Authorization: Bearer <access_token>" http://localhost:8585/api/v1/users/me
