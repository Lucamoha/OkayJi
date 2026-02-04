package com.okayji.identity.controller;

import com.okayji.common.ApiResponse;
import com.okayji.feed.dto.response.PostResponse;
import com.okayji.feed.service.PostService;
import com.okayji.identity.dto.request.ProfileUpdateRequest;
import com.okayji.identity.dto.response.ProfileResponse;
import com.okayji.identity.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
@AllArgsConstructor
@Tag(name = "Profile Controller")
public class ProfileController {

    private ProfileService profileService;
    private PostService postService;

    @GetMapping("/{userIdOrUsername}")
    @Operation(summary = "Get Users Profile by username or userId")
    ApiResponse<ProfileResponse> getUserProfile(@PathVariable String userIdOrUsername) {
        return ApiResponse.<ProfileResponse>builder()
                .success(true)
                .message("Get users profile success")
                .data(profileService.getUserProfile(userIdOrUsername))
                .build();
    }

    @GetMapping("/{userIdOrUsername}/posts")
    @Operation(summary = "Get Users posts by username or userId")
    ApiResponse<Page<PostResponse>> getPostsByUser(
            @PathVariable String userIdOrUsername,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.<Page<PostResponse>>builder()
                .success(true)
                .data(postService.getPostsByUser(userIdOrUsername, page, size))
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

    @PutMapping("/my-profile")
    @Operation(summary = "Update profile")
    ApiResponse<ProfileResponse> updateProfile(@RequestBody ProfileUpdateRequest profileUpdateRequest) {
        return ApiResponse.<ProfileResponse>builder()
                .success(true)
                .message("Update users profile success")
                .data(profileService.updateUserProfile(profileUpdateRequest))
                .build();
    }
}
