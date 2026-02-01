package com.okayji.identity.controller;

import com.okayji.common.ApiResponse;
import com.okayji.identity.dto.request.ProfileUpdateRequest;
import com.okayji.identity.dto.response.ProfileResponse;
import com.okayji.identity.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {

    private ProfileService profileService;

    @GetMapping("/{userId}")
    ApiResponse<ProfileResponse> getUserProfile(@PathVariable String userId) {
        return ApiResponse.<ProfileResponse>builder()
                .success(true)
                .message("Get users profile success")
                .data(profileService.getUserProfile(userId))
                .build();
    }

    @GetMapping("/my-profile")
    ApiResponse<ProfileResponse> getMyProfile() {
        return ApiResponse.<ProfileResponse>builder()
                .success(true)
                .message("Get users profile success")
                .data(profileService.getMyProfile())
                .build();
    }

    @PutMapping
    ApiResponse<ProfileResponse> updateProfile(@RequestBody ProfileUpdateRequest profileUpdateRequest) {
        return ApiResponse.<ProfileResponse>builder()
                .success(true)
                .message("Update users profile success")
                .data(profileService.updateUserProfile(profileUpdateRequest))
                .build();
    }
}
