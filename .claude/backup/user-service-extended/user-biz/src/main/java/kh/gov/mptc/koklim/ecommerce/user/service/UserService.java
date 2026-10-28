package kh.gov.mptc.koklim.ecommerce.user.service;

import kh.gov.mptc.koklim.ecommerce.user.dto.AuthenticatedUser;
import kh.gov.mptc.koklim.ecommerce.user.dto.request.ProfileUpdateRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.UserProfileResponse;

import java.util.UUID;

public interface UserService {

    // Profile of the caller. Created on first use (just-in-time provisioning from the Keycloak token).
    UserProfileResponse getOrCreateMyProfile(AuthenticatedUser me);

    UserProfileResponse updateMyProfile(AuthenticatedUser me, ProfileUpdateRequest request);

    UserProfileResponse getById(UUID id);

}
