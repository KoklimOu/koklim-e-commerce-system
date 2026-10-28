package kh.gov.mptc.koklim.ecommerce.user.service.impl;

import kh.gov.mptc.koklim.ecommerce.user.dto.request.AdminCreateUserRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.AdminUserResponse;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.AdminUserSummaryResponse;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.PageResponse;
import kh.gov.mptc.koklim.ecommerce.user.entity.UserProfile;
import kh.gov.mptc.koklim.ecommerce.user.config.KeycloakAdminClient;
import kh.gov.mptc.koklim.ecommerce.user.config.KeycloakUser;
import kh.gov.mptc.koklim.ecommerce.user.config.NewKeycloakUser;
import kh.gov.mptc.koklim.ecommerce.user.repository.UserProfileRepository;
import kh.gov.mptc.koklim.ecommerce.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private static final String DEFAULT_ROLE = "CUSTOMER";
    private static final int MAX_PAGE_SIZE = 100;

    private final KeycloakAdminClient keycloak;
    private final UserProfileRepository userProfileRepository;

    @Override
    public AdminUserResponse create(AdminCreateUserRequest request) {
        List<String> roles = (request.roles() == null || request.roles().isEmpty())
                ? List.of(DEFAULT_ROLE)
                : request.roles();

        UUID id = keycloak.createUser(new NewKeycloakUser(
                request.username(), request.email(), request.firstName(), request.lastName(),
                request.temporaryPassword(), true), roles);

        return getById(id);
    }

    @Override
    public PageResponse<AdminUserSummaryResponse> search(String search, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.clamp(size, 1, MAX_PAGE_SIZE);

        List<AdminUserSummaryResponse> content = keycloak.searchUsers(search, safePage * safeSize, safeSize).stream()
                .map(u -> AdminUserSummaryResponse.builder()
                        .id(u.id()).username(u.username()).email(u.email())
                        .firstName(u.firstName()).lastName(u.lastName()).enabled(u.enabled())
                        .build())
                .toList();
        long total = keycloak.countUsers(search);
        int totalPages = (int) Math.ceil((double) total / safeSize);

        return new PageResponse<>(content, safePage, safeSize, total, totalPages);
    }

    @Override
    public AdminUserResponse getById(UUID id) {
        KeycloakUser user = keycloak.getUser(id);
        List<String> roles = keycloak.getRealmRoles(id);
        UserProfile profile = userProfileRepository.findById(id).orElse(null);

        return AdminUserResponse.builder()
                .id(user.id()).username(user.username()).email(user.email())
                .firstName(user.firstName()).lastName(user.lastName()).enabled(user.enabled())
                .roles(roles)
                .displayName(profile == null ? null : profile.getDisplayName())
                .avatarUrl(profile == null ? null : profile.getAvatarUrl())
                .bio(profile == null ? null : profile.getBio())
                .build();
    }

    @Override
    public AdminUserResponse setEnabled(UUID id, boolean enabled) {
        keycloak.setEnabled(id, enabled);
        return getById(id);
    }

    @Override
    public AdminUserResponse setRoles(UUID id, List<String> roles) {
        TreeSet<String> current = new TreeSet<>(keycloak.getRealmRoles(id));
        TreeSet<String> wanted = new TreeSet<>(roles);

        List<String> toAdd = wanted.stream().filter(r -> !current.contains(r)).toList();
        List<String> toRemove = current.stream().filter(r -> !wanted.contains(r)).toList();

        if (!toAdd.isEmpty()) keycloak.addRealmRoles(id, toAdd);
        if (!toRemove.isEmpty()) keycloak.removeRealmRoles(id, toRemove);
        return getById(id);
    }

    @Override
    public void resetPassword(UUID id, String password, boolean temporary) {
        keycloak.resetPassword(id, password, temporary);
    }

    @Override
    public void delete(UUID id) {
        keycloak.deleteUser(id);              // Keycloak first: it is the source of truth
        userProfileRepository.deleteById(id); // then our profile (does nothing if there is none)
    }
}
