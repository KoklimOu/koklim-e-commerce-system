package kh.gov.mptc.koklim.ecommerce.user.service;

public interface KeycloakRoleService {
    void assignRole(String userId, String roleNameWithDelimiter);
}
