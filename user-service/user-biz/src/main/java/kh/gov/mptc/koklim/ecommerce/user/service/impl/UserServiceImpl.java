package kh.gov.mptc.koklim.ecommerce.user.service.impl;

import kh.gov.mptc.koklim.ecommerce.user.config.KeycloakAdminClientConfig;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserCreateRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserResponse;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserUpdateRequest;
import kh.gov.mptc.koklim.ecommerce.user.entity.UserEntity;
import kh.gov.mptc.koklim.ecommerce.user.exception.PasswordMismatchException;
import kh.gov.mptc.koklim.ecommerce.user.exception.UserNotFoundException;
import kh.gov.mptc.koklim.ecommerce.user.exception.UsernameConflictException;
import kh.gov.mptc.koklim.ecommerce.user.mapper.UserMapper;
import kh.gov.mptc.koklim.ecommerce.user.repository.UserRepository;
import kh.gov.mptc.koklim.ecommerce.user.service.KeycloakRoleService;
import kh.gov.mptc.koklim.ecommerce.user.service.KeycloakUserService;
import kh.gov.mptc.koklim.ecommerce.user.service.UserService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakUserService keycloakUserService;
    private final KeycloakRoleService keycloakRoleService;
    private final Keycloak keycloak;

    @Override
    public UserResponse create(UserCreateRequest userCreateRequest) {

        // Validate username
        if (userRepository.existsByUsername(userCreateRequest.username())) {
            throw new UsernameConflictException(HttpStatus.CONFLICT,
                    "Username already exists");
        }

        // Validate password
        if (!userCreateRequest.password().equals(userCreateRequest.confirmedPassword())) {
            throw new PasswordMismatchException(HttpStatus.BAD_REQUEST,
                    "Password does not match");
        }

        // 1) Save the user into Keycloak (identity, password)
        keycloakUserService.createKeycloakUser(toKeycloakUser(userCreateRequest));
        String keycloakUserId = keycloakUserService
                .getKeycloakUserByUsername(userCreateRequest.username()).getId();

        keycloakRoleService.assignRole(keycloakUserId, userCreateRequest.roleId());

        // 2) Then save the profile into our DB, using the Keycloak id
        try {
            UserEntity userEntity = userMapper.toEntity(userCreateRequest);
            userEntity.setId(keycloakUserId);
            userRepository.save(userEntity);

            return new UserResponse(
                    userEntity.getId(),
                    userCreateRequest.familyName(),
                    userCreateRequest.givenName(),
                    userEntity.getGender(),
                    userEntity.getBirthday(),
                    userEntity.getMobilePhone(),
                    userEntity.getAddress());
        } catch (RuntimeException e) {
            // Do not leave a Keycloak user without a profile: a retry would be blocked by "already exists"
            keycloakUserService.deleteKeycloakUserQuietly(keycloakUserId);
            throw e;
        }
    }


    private UserRepresentation toKeycloakUser(UserCreateRequest userCreateRequest) {
        UserRepresentation userRepresentation = userMapper.toRepresentation(userCreateRequest);
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(false);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(userCreateRequest.password());
        credentialRepresentation.setTemporary(false);
        userRepresentation.setCredentials(List.of(credentialRepresentation));
        return userRepresentation;
    }

    @Override
    public UserResponse update(String id, UserUpdateRequest userUpdateRequest) {
        //valid if user exists
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
            new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found")
        );

        userMapper.toEntity(userUpdateRequest, userEntity);
        userRepository.save(userEntity);

        //validate keycloak user exists
        UserRepresentation userRepresentation = keycloakUserService.getKeycloakUserByUsername(userEntity.getUsername());

        // attpempt to update keycloak user
        if (userUpdateRequest.familyName() != null || userUpdateRequest.givenName() != null) {
            if (userUpdateRequest.familyName() != null ) {
                userRepresentation.setFirstName(userUpdateRequest.familyName());
            }

            if (userUpdateRequest.givenName() != null ) {
                userRepresentation.setLastName(userUpdateRequest.givenName());
            }
        }



//        keycloakUserService.updateKeycloakUser();

        return userMapper.toResponse(userEntity, userRepresentation);
    }

    @Override
    public UserResponse getById(String id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found")
        );
        return userMapper.toResponse(
                userEntity, keycloakUserService.getKeycloakUserById(id));
    }
}
