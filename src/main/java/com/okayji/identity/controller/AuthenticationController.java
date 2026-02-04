package com.okayji.identity.controller;

import com.okayji.common.ApiResponse;
import com.okayji.identity.dto.request.AuthenticationRequest;
import com.okayji.identity.dto.request.UserCreationRequest;
import com.okayji.identity.dto.response.AuthenticationResponse;
import com.okayji.identity.dto.response.UserResponse;
import com.okayji.identity.service.AuthenticationService;
import com.okayji.identity.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j(topic = "AUTHENTICATION-CONTROLLER")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> logIn(@RequestBody AuthenticationRequest request) {
        log.info("User login request: username={}", request.getUsername());

        return ApiResponse.<AuthenticationResponse>builder()
                .success(true)
                .message("Login success")
                .data(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logOut(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(request, response);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Logout success")
                .build();
    }

    @PostMapping("/signup")
    ApiResponse<UserResponse> signUp(@RequestBody @Valid UserCreationRequest usercreationRequest) {
        log.info("User sign up request: username={}", usercreationRequest.getUsername());

        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User created successfully")
                .data(userService.create(usercreationRequest))
                .build();
    }
}
