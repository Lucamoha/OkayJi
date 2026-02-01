package com.okayji.identity.controller;

import com.okayji.common.ApiResponse;
import com.okayji.identity.dto.request.AuthenticationRequest;
import com.okayji.identity.dto.response.AuthenticationResponse;
import com.okayji.identity.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j(topic = "AUTHENTICATION-CONTROLLER")
@AllArgsConstructor
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> logIn(@RequestBody AuthenticationRequest request) {
        log.info("User login request: username=" + request.getUsername());

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
}
