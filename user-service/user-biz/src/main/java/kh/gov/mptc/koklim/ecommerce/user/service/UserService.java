package kh.gov.mptc.koklim.ecommerce.user.service;

import kh.gov.mptc.koklim.ecommerce.user.dto.UserCreateRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserResponse;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserUpdateRequest;

import java.util.List;

public interface UserService {

    UserResponse create(UserCreateRequest userCreateRequest);



    List<UserResponse> getUsersByRoleName(String roleName);
    List<UserResponse> getUsers();
    UserResponse getUserByUsername(String username);
    UserResponse update(String id, UserUpdateRequest userUpdateRequest);
    void delete(String id);
    UserResponse getUserById(String id);
}
