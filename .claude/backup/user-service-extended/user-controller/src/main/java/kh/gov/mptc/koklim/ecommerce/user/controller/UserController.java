package kh.gov.mptc.koklim.ecommerce.user.controller;

import jakarta.validation.Valid;
import kh.gov.mptc.koklim.ecommerce.user.dto.AuthenticatedUser;
import kh.gov.mptc.koklim.ecommerce.user.dto.request.ProfileUpdateRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.MyProfileResponse;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.UserProfileResponse;
import kh.gov.mptc.koklim.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Controller: no business rules here. Read the token -> call the service -> return a DTO.
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // The logged-in user: identity from the Keycloak token + public profile from our database
    @GetMapping("/me")
    public MyProfileResponse me(@AuthenticationPrincipal Jwt jwt, Authentication authentication) {
        AuthenticatedUser me = toAuthenticatedUser(jwt, authentication);
        return toMyProfile(me, userService.getOrCreateMyProfile(me));
    }

    @PutMapping("/me")
    public MyProfileResponse updateMe(@AuthenticationPrincipal Jwt jwt, Authentication authentication,
                                      @Valid @RequestBody ProfileUpdateRequest request) {
        AuthenticatedUser me = toAuthenticatedUser(jwt, authentication);
        return toMyProfile(me, userService.updateMyProfile(me, request));
    }

    // Public profile of any user (any logged-in user may call this)
    @GetMapping("/{userId}")
    public UserProfileResponse getById(@PathVariable UUID userId) {
        return userService.getById(userId);
    }

    private static AuthenticatedUser toAuthenticatedUser(Jwt jwt, Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring("ROLE_".length()))
                .sorted()
                .toList();
        return new AuthenticatedUser(
                UUID.fromString(jwt.getSubject()),
                jwt.getClaimAsString("preferred_username"),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("name"),
                roles);
    }

    private static MyProfileResponse toMyProfile(AuthenticatedUser me, UserProfileResponse profile) {
        return MyProfileResponse.builder()
                .id(profile.id())
                .username(profile.username())
                .displayName(profile.displayName())
                .avatarUrl(profile.avatarUrl())
                .bio(profile.bio())
                .email(me.email())
                .fullName(me.fullName())
                .roles(me.roles())
                .build();
    }
}
