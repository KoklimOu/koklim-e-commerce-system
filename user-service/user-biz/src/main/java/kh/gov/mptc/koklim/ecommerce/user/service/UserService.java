package kh.gov.mptc.koklim.ecommerce.user.service;

import kh.gov.mptc.koklim.ecommerce.user.dto.UserCreateRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserResponse;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserUpdateRequest;

public interface UserService {

    UserResponse create(UserCreateRequest userCreateRequest);

    UserResponse update(String id, UserUpdateRequest userUpdateRequest);

    UserResponse getById(String id);
}
