#!/usr/bin/env bash
# One-time Keycloak setup for user-service's admin client. Safe to run again (it skips what already exists).
#
#   bash user-service/docs/setup-keycloak-admin-client.sh
#
# What it does in realm "ecommerce":
#   1. creates realm roles ADMIN and CUSTOMER (if missing)
#   2. creates client "user-service-admin": confidential, service account ON, no browser login
#   3. gives its service account the realm-management roles the admin client needs
#   4. prints the client secret for KEYCLOAK_ADMIN_CLIENT_SECRET
#
# Needs: bash, curl, python3 (only to read JSON). Keycloak admin login = a master-realm admin.
# Settings (override with environment variables):
#   KEYCLOAK_URL=http://localhost:8180   KC_REALM=ecommerce
#   KC_ADMIN_USER=admin   KC_ADMIN_PASSWORD=admin   KC_ADMIN_CLIENT_ID=user-service-admin
set -euo pipefail

KC_URL=${KEYCLOAK_URL:-http://localhost:8180}
REALM=${KC_REALM:-ecommerce}
KC_ADMIN_USER=${KC_ADMIN_USER:-admin}
KC_ADMIN_PASSWORD=${KC_ADMIN_PASSWORD:-admin}
CLIENT_ID=${KC_ADMIN_CLIENT_ID:-user-service-admin}

REALM_ROLES=(ADMIN CUSTOMER)
# realm-management roles: manage users + read users + read realm roles (to resolve role names)
MANAGEMENT_ROLES=(manage-users view-users query-users view-realm)

fail() { echo "FAILED: $*" >&2; exit 1; }

# json_get "d['key']" < json   (the expression is written in this script, never taken from user input)
json_get() { python3 -c "import sys,json; d=json.load(sys.stdin); print($1)" 2>/dev/null; }

# call METHOD PATH [JSON]  ->  sets RESP_CODE and RESP_BODY (path is relative to /admin/realms/$REALM)
call() {
  local method=$1 path=$2 body=${3:-} out
  if [[ -n $body ]]; then
    out=$(curl -sS -X "$method" -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
          -d "$body" -w $'\n%{http_code}' "$KC_URL/admin/realms/$REALM$path") || fail "cannot reach $KC_URL"
  else
    out=$(curl -sS -X "$method" -H "Authorization: Bearer $TOKEN" \
          -w $'\n%{http_code}' "$KC_URL/admin/realms/$REALM$path") || fail "cannot reach $KC_URL"
  fi
  RESP_CODE=${out##*$'\n'}
  RESP_BODY=${out%$'\n'*}
}

echo "1/5 Logging in to Keycloak ($KC_URL) as '$KC_ADMIN_USER' ..."
TOKEN=$(curl -fsS -d client_id=admin-cli -d grant_type=password \
          --data-urlencode "username=$KC_ADMIN_USER" --data-urlencode "password=$KC_ADMIN_PASSWORD" \
          "$KC_URL/realms/master/protocol/openid-connect/token" | json_get "d['access_token']") \
  || fail "admin login failed. Is Keycloak running? Set KC_ADMIN_USER / KC_ADMIN_PASSWORD if they are not admin/admin"

echo "2/5 Realm roles in '$REALM' ..."
for role in "${REALM_ROLES[@]}"; do
  call POST /roles "{\"name\":\"$role\"}"
  case $RESP_CODE in
    201) echo "  created  $role" ;;
    409) echo "  exists   $role" ;;
    404) fail "realm '$REALM' not found (set KC_REALM)" ;;
    *)   fail "creating role $role -> HTTP $RESP_CODE: $RESP_BODY" ;;
  esac
done

echo "3/5 Client '$CLIENT_ID' ..."
call POST /clients "{\"clientId\":\"$CLIENT_ID\",\"protocol\":\"openid-connect\",\"enabled\":true,\
\"publicClient\":false,\"clientAuthenticatorType\":\"client-secret\",\"serviceAccountsEnabled\":true,\
\"standardFlowEnabled\":false,\"implicitFlowEnabled\":false,\"directAccessGrantsEnabled\":false}"
case $RESP_CODE in
  201) echo "  created" ;;
  409) echo "  exists" ;;
  *)   fail "creating client -> HTTP $RESP_CODE: $RESP_BODY" ;;
esac

call GET "/clients?clientId=$CLIENT_ID"
CLIENT_UUID=$(echo "$RESP_BODY" | json_get "d[0]['id']") || fail "client '$CLIENT_ID' not found after creating it"

echo "4/5 Service account roles (realm-management) ..."
call GET "/clients/$CLIENT_UUID/service-account-user"
SA_USER_ID=$(echo "$RESP_BODY" | json_get "d['id']") || fail "client has no service account user"

call GET "/clients?clientId=realm-management"
MGMT_UUID=$(echo "$RESP_BODY" | json_get "d[0]['id']") || fail "realm-management client not found"

ROLE_REPS=""
for role in "${MANAGEMENT_ROLES[@]}"; do
  call GET "/clients/$MGMT_UUID/roles/$role"
  [[ $RESP_CODE == 200 ]] || fail "realm-management role '$role' -> HTTP $RESP_CODE: $RESP_BODY"
  ROLE_REPS+="${ROLE_REPS:+,}$RESP_BODY"
done
# Adding roles that are already assigned is harmless
call POST "/users/$SA_USER_ID/role-mappings/clients/$MGMT_UUID" "[$ROLE_REPS]"
[[ $RESP_CODE == 204 ]] || fail "assigning roles -> HTTP $RESP_CODE: $RESP_BODY"
echo "  assigned ${MANAGEMENT_ROLES[*]}"

echo "5/5 Client secret ..."
call GET "/clients/$CLIENT_UUID/client-secret"
SECRET=$(echo "$RESP_BODY" | json_get "d['value']") || fail "could not read the client secret"

cat <<EOF

Done. Start user-service with this in its environment (do NOT commit it):

  KEYCLOAK_ADMIN_CLIENT_SECRET=$SECRET

IntelliJ: Run > Edit Configurations > UserServiceApplication > Environment variables.
Then check everything:  bash user-service/docs/check-env.sh
EOF
