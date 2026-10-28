package kh.gov.mptc.koklim.ecommerce.user.service;

import kh.gov.mptc.koklim.ecommerce.user.dto.UserCreateRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserResponse;

public interface UserService {

    UserResponse create(UserCreateRequest userCreateRequest);
}
