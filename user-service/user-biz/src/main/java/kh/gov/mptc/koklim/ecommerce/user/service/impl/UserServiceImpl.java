package kh.gov.mptc.koklim.ecommerce.user.service.impl;

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
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakUserService keycloakUserService;
    private final KeycloakRoleService keycloakRoleService;

    @Override
    public UserResponse create(UserCreateRequest userCreateRequest) {

        if (userRepository.existsByUsername(userCreateRequest.username())) {
            throw new UsernameConflictException(HttpStatus.CONFLICT,
                    "Username already exists");
        }

        if (!userCreateRequest.password().equals(userCreateRequest.confirmedPassword())) {
            throw new PasswordMismatchException(HttpStatus.BAD_REQUEST,
                    "Password does not match");
        }

        keycloakUserService.createKeycloakUser(toKeycloakUser(userCreateRequest));
        String keycloakUserId = keycloakUserService
                .getKeycloakUserByUsername(userCreateRequest.username()).getId();

        keycloakRoleService.assignRole(keycloakUserId, userCreateRequest.roleId());

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
            keycloakUserService.deleteKeycloakUserQuietly(keycloakUserId);
            throw e;
        }
    }

    @Override
    public List<UserResponse> getUsersByRoleName(String roleName) {
        List<UserRepresentation> members = keycloakRoleService.getUsersByRoleName(roleName);
        List<String> ids = members.stream().map(UserRepresentation::getId).toList();
        return toResponses(userRepository.findAllById(ids), members);
    }

    @Override
    public List<UserResponse> getUsers() {
        return toResponses(userRepository.findAll(), keycloakUserService.getKeycloakUsers());
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found")
        );
        return userMapper.toResponse(
                userEntity, keycloakUserService.getKeycloakUserById(userEntity.getId()));
    }

    @Override
    public UserResponse update(String id, UserUpdateRequest userUpdateRequest) {
        //valid if user exists
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
            new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found")
        );

        UserRepresentation userRepresentation = keycloakUserService.getKeycloakUserById(id);

        if (userUpdateRequest.familyName() != null || userUpdateRequest.givenName() != null) {
            if (userUpdateRequest.familyName() != null) {
                userRepresentation.setLastName(userUpdateRequest.familyName());
            }
            if (userUpdateRequest.givenName() != null) {
                userRepresentation.setFirstName(userUpdateRequest.givenName());
            }
            keycloakUserService.updateKeycloakUser(id, userRepresentation);
        }

        userMapper.toEntity(userUpdateRequest, userEntity);
        userRepository.save(userEntity);

        return userMapper.toResponse(userEntity, userRepresentation);
    }

    @Override
    public void delete(String id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found")
        );

        // 1) Delete from Keycloak first: if it fails, the user keeps both login and profile
        keycloakUserService.deleteKeycloakUser(id);

        // 2) Then delete the profile from our DB
        userRepository.delete(userEntity);
    }

    @Override
    public UserResponse getUserById(String id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found")
        );
        return userMapper.toResponse(
                userEntity, keycloakUserService.getKeycloakUserById(id));
    }


    //  HELPS METHODS

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

    // Pairs each profile with its Keycloak user by id (names come from Keycloak)
    private List<UserResponse> toResponses(List<UserEntity> userEntities, List<UserRepresentation> userRepresentations) {
        Map<String, UserRepresentation> repById = userRepresentations.stream()
                .collect(Collectors.toMap(UserRepresentation::getId, rep -> rep));
        return userEntities.stream()
                .map(entity -> userMapper.toResponse(entity, repById.get(entity.getId())))
                .toList();
    }

    // END OF HELPS METHODS
}
