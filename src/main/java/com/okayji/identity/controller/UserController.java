package com.okayji.identity.controller;

import com.okayji.identity.dto.request.UserChangePasswordRequest;
import com.okayji.common.ApiResponse;
import com.okayji.identity.dto.request.UserChangeUsernameRequest;
import com.okayji.identity.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/change-password")
    ApiResponse<?> changePassword(@RequestBody @Valid UserChangePasswordRequest request) {
        userService.changePassword(request);

        return ApiResponse.builder()
                .success(true)
                .message("Change password successfully")
                .build();
    }

    @PostMapping("/change-username")
    ApiResponse<?> changeUsername(@RequestBody @Valid UserChangeUsernameRequest request) {
        userService.changeUsername(request);

        return ApiResponse.builder()
                .success(true)
                .message("Change username successfully")
                .build();
    }
}
