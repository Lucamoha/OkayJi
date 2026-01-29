package com.okayji.identity.controller;

import com.okayji.dto.ApiResponse;
import com.okayji.identity.dto.response.UserProfileResponse;
import com.okayji.identity.service.UserProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@Slf4j(topic = "USER-PROFILE-CONTROLLER")
@AllArgsConstructor
public class UserProfileController {

    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    ApiResponse<UserProfileResponse> getUserProfile(@PathVariable String userId) {
        return ApiResponse.<UserProfileResponse>builder()
                .success(true)
                .message("Get users profile success")
                .data(userProfileService.getUserProfile(userId))
                .build();
    }

    @GetMapping("/my-profile")
    ApiResponse<UserProfileResponse> getMyProfile() {
        return ApiResponse.<UserProfileResponse>builder()
                .success(true)
                .message("Get users profile success")
                .data(userProfileService.getMyProfile())
                .build();
    }
}
