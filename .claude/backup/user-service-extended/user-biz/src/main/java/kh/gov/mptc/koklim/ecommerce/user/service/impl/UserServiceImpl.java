package kh.gov.mptc.koklim.ecommerce.user.service.impl;

import kh.gov.mptc.koklim.ecommerce.user.dto.AuthenticatedUser;
import kh.gov.mptc.koklim.ecommerce.user.dto.request.ProfileUpdateRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.UserProfileResponse;
import kh.gov.mptc.koklim.ecommerce.user.entity.UserProfile;
import kh.gov.mptc.koklim.ecommerce.user.exception.UserNotFoundException;
import kh.gov.mptc.koklim.ecommerce.user.mapper.UserProfileMapper;
import kh.gov.mptc.koklim.ecommerce.user.repository.UserProfileRepository;
import kh.gov.mptc.koklim.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)   // default for reads; write methods override below
public class UserServiceImpl implements UserService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    @Transactional
    public UserProfileResponse getOrCreateMyProfile(AuthenticatedUser me) {
        return userProfileMapper.toResponse(findOrProvision(me));
    }

    @Override
    @Transactional
    public UserProfileResponse updateMyProfile(AuthenticatedUser me, ProfileUpdateRequest request) {
        UserProfile profile = findOrProvision(me);
        userProfileMapper.updateEntity(request, profile);
        // No save() needed: "profile" is managed, JPA flushes changes at commit (dirty checking)
        return userProfileMapper.toResponse(profile);
    }

    @Override
    public UserProfileResponse getById(UUID id) {
        return userProfileMapper.toResponse(findOrThrow(id));
    }

    private UserProfile findOrProvision(AuthenticatedUser me) {
        UserProfile profile = userProfileRepository.findById(me.id()).orElseGet(() -> {
            userProfileRepository.insertIfAbsent(me.id(), me.username(), initialDisplayName(me));
            return userProfileRepository.findById(me.id()).orElseThrow(() -> new UserNotFoundException(me.id()));
        });
        // Keycloak is the source of truth for the username: keep our copy in step
        if (!profile.getUsername().equals(me.username())) {
            profile.setUsername(me.username());
        }
        return profile;
    }

    private String initialDisplayName(AuthenticatedUser me) {
        return (me.fullName() == null || me.fullName().isBlank()) ? me.username() : me.fullName();
    }

    private UserProfile findOrThrow(UUID id) {
        return userProfileRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
