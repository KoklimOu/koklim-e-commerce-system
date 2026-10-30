package kh.gov.mptc.koklim.ecommerce.user.controller;

import jakarta.validation.Valid;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserCreateRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserResponse;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserUpdateRequest;
import kh.gov.mptc.koklim.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponse create(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        return userService.create(userCreateRequest);
    }

    @PatchMapping("/{id}")
    public UserResponse update(@PathVariable String id, @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.update(id, userUpdateRequest);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable String id) {
        return userService.getById(id);
    }
}
