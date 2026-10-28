package kh.gov.mptc.koklim.ecommerce.user.service.impl;

import kh.gov.mptc.koklim.ecommerce.user.dto.UserCreateRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserResponse;
import kh.gov.mptc.koklim.ecommerce.user.entity.UserEntity;
import kh.gov.mptc.koklim.ecommerce.user.exception.PasswordMismatchException;
import kh.gov.mptc.koklim.ecommerce.user.exception.UsernameConflictException;
import kh.gov.mptc.koklim.ecommerce.user.mapper.UserMapper;
import kh.gov.mptc.koklim.ecommerce.user.repository.UserRepository;
import kh.gov.mptc.koklim.ecommerce.user.service.KeycloakUserService;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock UserMapper userMapper;
    @Mock KeycloakUserService keycloakUserService;
    @InjectMocks UserServiceImpl userService;

    private final UserCreateRequest request = new UserCreateRequest(
            "koklim", "koklim@example.com", "Ou", "Koklim", "Passw0rd!", "Passw0rd!", "CUSTOMER");

    @Test
    void create_throwsConflict_whenUsernameExists() {
        when(userRepository.existsByUsername("koklim")).thenReturn(true);

        assertThatThrownBy(() -> userService.create(request)).isInstanceOf(UsernameConflictException.class);
        verifyNoInteractions(keycloakUserService);
    }

    @Test
    void create_throwsBadRequest_whenPasswordsDiffer() {
        UserCreateRequest mismatch = new UserCreateRequest(
                "koklim", "koklim@example.com", "Ou", "Koklim", "Passw0rd!", "other", "CUSTOMER");

        assertThatThrownBy(() -> userService.create(mismatch)).isInstanceOf(PasswordMismatchException.class);
        verifyNoInteractions(keycloakUserService);
    }

    @Test
    void create_savesProfileWithKeycloakId() {
        stubKeycloakCreate("kc-id-1");
        UserEntity entity = new UserEntity();
        entity.setUsername("koklim");
        when(userMapper.toEntity(request)).thenReturn(entity);

        UserResponse result = userService.create(request);

        assertThat(result.id()).isEqualTo("kc-id-1");
        assertThat(result.familyName()).isEqualTo("Ou");
        verify(userRepository).save(entity);
        verify(keycloakUserService, never()).deleteKeycloakUserQuietly(any());
    }

    @Test
    void create_removesKeycloakUser_whenSavingProfileFails() {
        stubKeycloakCreate("kc-id-1");
        when(userMapper.toEntity(request)).thenReturn(new UserEntity());
        when(userRepository.save(any())).thenThrow(new IllegalStateException("db down"));

        assertThatThrownBy(() -> userService.create(request)).isInstanceOf(IllegalStateException.class);

        verify(keycloakUserService).deleteKeycloakUserQuietly("kc-id-1");
    }

    @Test
    void create_savesNothing_whenKeycloakFails() {
        when(userMapper.toRepresentation(request)).thenReturn(new UserRepresentation());
        doThrow(new RuntimeException("keycloak failed")).when(keycloakUserService).createKeycloakUser(any());

        assertThatThrownBy(() -> userService.create(request)).isInstanceOf(RuntimeException.class);

        verify(userRepository, never()).save(any());
        verify(keycloakUserService, never()).deleteKeycloakUserQuietly(any());
    }

    private void stubKeycloakCreate(String keycloakId) {
        when(userMapper.toRepresentation(request)).thenReturn(new UserRepresentation());
        UserRepresentation found = new UserRepresentation();
        found.setId(keycloakId);
        when(keycloakUserService.getKeycloakUserByUsername("koklim")).thenReturn(found);
    }
}
