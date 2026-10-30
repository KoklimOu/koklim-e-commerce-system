package kh.gov.mptc.koklim.ecommerce.user.service;

import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakRoleService {
    void assignRole(String userId, String roleNameWithDelimiter);

    List<UserRepresentation> getUsersByRoleName(String roleName);
}
