package kh.gov.mptc.koklim.ecommerce.user.service.impl;

import kh.gov.mptc.koklim.ecommerce.user.config.KeycloakAdminClientConfig;
import kh.gov.mptc.koklim.ecommerce.user.service.KeycloakRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakRoleServiceImpl implements KeycloakRoleService {
    private final Keycloak keycloak;
    @Override
    public void assignRole(String userId, String roleNameWithDelimiter) {

        String[] roleNameList = roleNameWithDelimiter.split(",");
        List<RoleRepresentation> roleRepresentationList = Arrays.stream(roleNameList)
                .map(String::trim)
                .filter(roleName -> !roleName.isEmpty())
                .map(this::getKeyCloakRoleByName)
                .toList();

        keycloak.realm(KeycloakAdminClientConfig.REALM_NAME)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(roleRepresentationList);
    }

    private RoleRepresentation getKeyCloakRoleByName(String roleName) {
        return keycloak.realm(KeycloakAdminClientConfig.REALM_NAME)
                .roles()
                .get(roleName).toRepresentation();
    }
}
