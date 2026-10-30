package kh.gov.mptc.koklim.ecommerce.user.service.impl;

import jakarta.ws.rs.NotFoundException;
import kh.gov.mptc.koklim.ecommerce.user.config.KeycloakAdminClientConfig;
import kh.gov.mptc.koklim.ecommerce.user.exception.KeycloakException;
import kh.gov.mptc.koklim.ecommerce.user.service.KeycloakRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
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

    @Override
    public List<UserRepresentation> getUsersByRoleName(String roleName) {
        try {
            return keycloak.realm(KeycloakAdminClientConfig.REALM_NAME)
                    .roles()
                    .get(roleName)
                    .getUserMembers();
        } catch (NotFoundException e) {
            throw new KeycloakException(HttpStatus.NOT_FOUND, "Role not found: " + roleName);
        }
    }

    private RoleRepresentation getKeyCloakRoleByName(String roleName) {
        return keycloak.realm(KeycloakAdminClientConfig.REALM_NAME)
                .roles()
                .get(roleName).toRepresentation();
    }
}
