package kh.gov.mptc.koklim.ecommerce.user.controller;

import jakarta.validation.Valid;
import kh.gov.mptc.koklim.ecommerce.user.dto.request.AdminCreateUserRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.request.AdminResetPasswordRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.request.AdminSetEnabledRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.request.AdminSetRolesRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.AdminUserResponse;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.AdminUserSummaryResponse;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.PageResponse;
import kh.gov.mptc.koklim.ecommerce.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// Admin only (see SecurityConfig). Everything here changes or reads identity data in Keycloak.
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserAdminService userAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminUserResponse create(@Valid @RequestBody AdminCreateUserRequest request) {
        return userAdminService.create(request);
    }

    @GetMapping
    public PageResponse<AdminUserSummaryResponse> search(@RequestParam(required = false) String search,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size) {
        return userAdminService.search(search, page, size);
    }

    @GetMapping("/{userId}")
    public AdminUserResponse getById(@PathVariable UUID userId) {
        return userAdminService.getById(userId);
    }

    @PatchMapping("/{userId}/enabled")
    public AdminUserResponse setEnabled(@PathVariable UUID userId,
                                        @Valid @RequestBody AdminSetEnabledRequest request) {
        return userAdminService.setEnabled(userId, request.enabled());
    }

    @PutMapping("/{userId}/roles")
    public AdminUserResponse setRoles(@PathVariable UUID userId,
                                      @Valid @RequestBody AdminSetRolesRequest request) {
        return userAdminService.setRoles(userId, request.roles());
    }

    @PostMapping("/{userId}/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@PathVariable UUID userId,
                              @Valid @RequestBody AdminResetPasswordRequest request) {
        userAdminService.resetPassword(userId, request.password(), !Boolean.FALSE.equals(request.temporary()));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID userId) {
        userAdminService.delete(userId);
    }
}
