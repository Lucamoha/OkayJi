package com.okayji.identity.controller;

import com.okayji.identity.dto.request.UserCreationRequest;
import com.okayji.dto.ApiResponse;
import com.okayji.identity.dto.response.UserResponse;
import com.okayji.identity.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j(topic = "USER-CONTROLLER")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest usercreationRequest) {
        log.info("User creation request: username={}", usercreationRequest.getUsername());

        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User created successfully")
                .data(userService.create(usercreationRequest))
                .build();
    }

}
