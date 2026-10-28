package kh.gov.mptc.koklim.ecommerce.user.service.impl;

import kh.gov.mptc.koklim.ecommerce.user.dto.AuthenticatedUser;
import kh.gov.mptc.koklim.ecommerce.user.dto.request.ProfileUpdateRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.UserProfileResponse;
import kh.gov.mptc.koklim.ecommerce.user.entity.UserProfile;
import kh.gov.mptc.koklim.ecommerce.user.exception.UserNotFoundException;
import kh.gov.mptc.koklim.ecommerce.user.mapper.UserProfileMapper;
import kh.gov.mptc.koklim.ecommerce.user.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock UserProfileRepository userProfileRepository;
    @Mock UserProfileMapper userProfileMapper;
    @InjectMocks UserServiceImpl userService;

    private final UUID id = UUID.randomUUID();
    private final AuthenticatedUser me =
            new AuthenticatedUser(id, "koklim", "koklim@example.com", "Kok Lim", List.of("CUSTOMER"));

    private UserProfile profile(String username) {
        UserProfile p = new UserProfile();
        p.setId(id);
        p.setUsername(username);
        return p;
    }

    @Test
    void getOrCreateMyProfile_createsProfile_onFirstCall() {
        UserProfile created = profile("koklim");
        when(userProfileRepository.findById(id)).thenReturn(Optional.empty(), Optional.of(created));
        UserProfileResponse expected = UserProfileResponse.builder().username("koklim").build();
        when(userProfileMapper.toResponse(created)).thenReturn(expected);

        assertThat(userService.getOrCreateMyProfile(me)).isEqualTo(expected);

        // display name starts as the Keycloak full name
        verify(userProfileRepository).insertIfAbsent(id, "koklim", "Kok Lim");
    }

    @Test
    void getOrCreateMyProfile_doesNotInsert_whenProfileExists() {
        UserProfile existing = profile("koklim");
        when(userProfileRepository.findById(id)).thenReturn(Optional.of(existing));

        userService.getOrCreateMyProfile(me);

        verify(userProfileRepository, never()).insertIfAbsent(any(), any(), any());
    }

    @Test
    void getOrCreateMyProfile_syncsUsername_whenChangedInKeycloak() {
        UserProfile existing = profile("old-name");
        when(userProfileRepository.findById(id)).thenReturn(Optional.of(existing));

        userService.getOrCreateMyProfile(me);

        assertThat(existing.getUsername()).isEqualTo("koklim");
    }

    @Test
    void updateMyProfile_copiesEditableFields() {
        UserProfile existing = profile("koklim");
        ProfileUpdateRequest request = ProfileUpdateRequest.builder().displayName("KL").build();
        when(userProfileRepository.findById(id)).thenReturn(Optional.of(existing));

        userService.updateMyProfile(me, request);

        verify(userProfileMapper).updateEntity(request, existing);
    }

    @Test
    void getById_throws_whenMissing() {
        when(userProfileRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(id)).isInstanceOf(UserNotFoundException.class);
    }
}
