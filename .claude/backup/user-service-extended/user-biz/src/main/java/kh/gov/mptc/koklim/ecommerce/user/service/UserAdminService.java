package kh.gov.mptc.koklim.ecommerce.user.service;

import kh.gov.mptc.koklim.ecommerce.user.dto.request.AdminCreateUserRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.AdminUserResponse;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.AdminUserSummaryResponse;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.PageResponse;

import java.util.List;
import java.util.UUID;

// Admin operations. Identity lives in Keycloak, so every write goes through the Keycloak admin client.
public interface UserAdminService {

    AdminUserResponse create(AdminCreateUserRequest request);

    PageResponse<AdminUserSummaryResponse> search(String search, int page, int size);

    AdminUserResponse getById(UUID id);

    AdminUserResponse setEnabled(UUID id, boolean enabled);

    AdminUserResponse setRoles(UUID id, List<String> roles);

    void resetPassword(UUID id, String password, boolean temporary);

    // Deletes the Keycloak account and the stored profile
    void delete(UUID id);
}
