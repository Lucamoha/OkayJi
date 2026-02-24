package com.okayji.identity.controller;

import com.okayji.common.ApiResponse;
import com.okayji.feed.dto.response.PostResponse;
import com.okayji.feed.service.PostService;
import com.okayji.identity.dto.request.ProfileUpdateRequest;
import com.okayji.identity.dto.response.ProfileResponse;
import com.okayji.identity.entity.User;
import com.okayji.identity.service.ProfileService;
import com.okayji.utils.CurrentUser;
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
    ApiResponse<ProfileResponse> getUserProfile(@PathVariable String userIdOrUsername,
                                                @CurrentUser User currentUser) {
        return ApiResponse.<ProfileResponse>builder()
                .success(true)
                .message("Get users profile success")
                .data(profileService.getUserProfile(currentUser.getId(), userIdOrUsername))
                .build();
    }

    @GetMapping("/{userIdOrUsername}/posts")
    @Operation(summary = "Get Users posts by username or userId")
    ApiResponse<Page<PostResponse>> getPostsByUser(@PathVariable String userIdOrUsername,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size,
                                                   @CurrentUser User currentUser
    ) {
        return ApiResponse.<Page<PostResponse>>builder()
                .success(true)
                .data(postService.getPostsByUser(currentUser.getId(), userIdOrUsername, page, size))
                .build();
    }

    @GetMapping("/my-profile")
    ApiResponse<ProfileResponse> getMyProfile(@CurrentUser User currentUser) {
        return ApiResponse.<ProfileResponse>builder()
                .success(true)
                .message("Get users profile success")
                .data(profileService.getUserProfile(currentUser.getId(), currentUser.getId()))
                .build();
    }

    @PutMapping("/my-profile")
    @Operation(summary = "Update profile")
    ApiResponse<ProfileResponse> updateProfile(@RequestBody ProfileUpdateRequest profileUpdateRequest,
                                               @CurrentUser User currentUser) {
        return ApiResponse.<ProfileResponse>builder()
                .success(true)
                .message("Update users profile success")
                .data(profileService.updateUserProfile(currentUser.getId(), profileUpdateRequest))
                .build();
    }
}
