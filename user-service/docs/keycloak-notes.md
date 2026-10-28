# Keycloak and user-service

## Who owns what

| Data | Lives in |
|---|---|
| username, email, first/last name, password, roles, enabled | **Keycloak** (identity and security) |
| gender, birthday, mobile phone, address (profile) | **user-service database**, table `users` |

The row id **is** the Keycloak user id. Creating a user does two steps:
1. create the user in Keycloak (admin client, service account),
2. save the profile row in our DB with the Keycloak id.
If step 2 fails, the Keycloak user is deleted again so a retry is not blocked.

## Endpoint (through the gateway, port 8585)

`POST /api/v1/users`

    {
      "username": "koklim", "email": "koklim@example.com",
      "familyName": "Ou", "givenName": "Koklim",
      "password": "Passw0rd!", "confirmedPassword": "Passw0rd!",
      "roleId": "CUSTOMER"
    }

| Result | When |
|---|---|
| 201 + user | created |
| 400 | validation failed, or password and confirmedPassword differ |
| 409 | username already exists (DB or Keycloak) |
| 502 | Keycloak is down, or the admin client secret / roles are wrong |

`roleId` is required by the request but not used yet (role assignment comes later).

## Admin client setup (one time)

user-service calls Keycloak with its own **service account** (library `org.keycloak:keycloak-admin-client`).
Run the script (Keycloak must be running; safe to run again):

    bash user-service/docs/setup-keycloak-admin-client.sh

It creates realm roles `ADMIN` + `CUSTOMER`, the client `ecommerce-admin-client` (service account on, no browser login),
gives the service account `manage-users`, `view-users`, `query-users`, `view-realm` from `realm-management`,
and prints the client secret. Defaults: `http://localhost:8180`, realm `ecommerce`, master admin `admin` / `admin`
(override with `KEYCLOAK_URL`, `KC_REALM`, `KC_ADMIN_USER`, `KC_ADMIN_PASSWORD`).

Start user-service with the secret in the environment (never commit it):

    KEYCLOAK_ADMIN_CLIENT_SECRET=<secret>

IntelliJ: Run > Edit Configurations > UserServiceApplication > Environment variables.
Check everything:

    KEYCLOAK_ADMIN_CLIENT_SECRET=<secret> bash user-service/docs/check-env.sh
