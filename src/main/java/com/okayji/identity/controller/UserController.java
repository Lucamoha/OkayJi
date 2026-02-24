package com.okayji.identity.controller;

import com.okayji.identity.dto.request.UserChangePasswordRequest;
import com.okayji.common.ApiResponse;
import com.okayji.identity.dto.request.UserChangeUsernameRequest;
import com.okayji.identity.entity.User;
import com.okayji.identity.service.UserService;
import com.okayji.utils.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Tag(name = "User Controller")
public class UserController {
    private final UserService userService;

    @PostMapping("/change-password")
    @Operation(summary = "Change users password")
    ApiResponse<?> changePassword(@RequestBody @Valid UserChangePasswordRequest request,
                                  @CurrentUser User currentUser) {
        userService.changePassword(currentUser.getId(), request);

        return ApiResponse.builder()
                .success(true)
                .message("Change password successfully")
                .build();
    }

    @PostMapping("/change-username")
    @Operation(summary = "Change users username")
    ApiResponse<?> changeUsername(@RequestBody @Valid UserChangeUsernameRequest request,
                                  @CurrentUser User currentUser) {
        userService.changeUsername(currentUser.getId(), request);

        return ApiResponse.builder()
                .success(true)
                .message("Change username successfully")
                .build();
    }
}
