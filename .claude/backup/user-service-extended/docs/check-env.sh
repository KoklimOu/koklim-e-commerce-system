#!/usr/bin/env bash
# Quick check that everything user-service needs is up. Run from anywhere:
#   bash user-service/docs/check-env.sh
# Optional: KEYCLOAK_ADMIN_CLIENT_SECRET=<secret> bash user-service/docs/check-env.sh   (also tests the admin client)
PG_HOST=${PG_HOST:-localhost}; PG_PORT=${PG_PORT:-8432}
NACOS=${NACOS:-localhost:8848}
KC_URL=${KEYCLOAK_URL:-http://localhost:8180}; REALM=${KC_REALM:-ecommerce}
ADMIN_CLIENT=${KC_ADMIN_CLIENT_ID:-user-service-admin}
ok(){ echo "  OK   $1"; }; bad(){ echo "  FAIL $1"; FAILED=1; }

echo "Postgres $PG_HOST:$PG_PORT"
(echo > /dev/tcp/$PG_HOST/$PG_PORT) 2>/dev/null && ok "port open" || bad "port closed - start Postgres"
if command -v psql >/dev/null; then
  PGPASSWORD=mypassword psql -h $PG_HOST -p $PG_PORT -U ecommerce_user -d db_user -c 'select 1' >/dev/null 2>&1 \
    && ok "db_user reachable as ecommerce_user" \
    || bad "cannot open db_user - run docs/db-setup.sql"
fi

echo "Nacos $NACOS"
curl -fsS -m 5 "http://$NACOS/nacos/" >/dev/null 2>&1 && ok "console reachable" || bad "Nacos not reachable"
echo "  note: namespace 'dev' must exist in Nacos (Namespace menu) because the service uses spring.profiles.active as namespace"

echo "Keycloak $KC_URL (realm $REALM)"
curl -fsS -m 5 "$KC_URL/realms/$REALM/.well-known/openid-configuration" >/dev/null 2>&1 \
  && ok "realm reachable (token issuer)" || bad "realm '$REALM' not reachable - start Keycloak / check the realm name"
if [[ -n "$KEYCLOAK_ADMIN_CLIENT_SECRET" ]]; then
  TOKEN=$(curl -fsS -m 5 -d grant_type=client_credentials -d "client_id=$ADMIN_CLIENT" \
            --data-urlencode "client_secret=$KEYCLOAK_ADMIN_CLIENT_SECRET" \
            "$KC_URL/realms/$REALM/protocol/openid-connect/token" 2>/dev/null \
          | python3 -c "import sys,json; print(json.load(sys.stdin)['access_token'])" 2>/dev/null)
  if [[ -z "$TOKEN" ]]; then
    bad "admin client '$ADMIN_CLIENT' could not get a token - wrong secret? run docs/setup-keycloak-admin-client.sh"
  else
    ok "admin client got a token"
    CODE=$(curl -s -o /dev/null -w '%{http_code}' -m 5 -H "Authorization: Bearer $TOKEN" "$KC_URL/admin/realms/$REALM/users/count")
    [[ $CODE == 200 ]] && ok "admin client may read users" \
      || bad "admin client got HTTP $CODE on /users/count - check its service account roles (realm-management)"
  fi
else
  echo "  skip admin client test (set KEYCLOAK_ADMIN_CLIENT_SECRET to include it)"
fi
exit ${FAILED:-0}
