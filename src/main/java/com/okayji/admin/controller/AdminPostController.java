package com.okayji.admin.controller;

import com.okayji.admin.service.AdminPostService;
import com.okayji.common.ApiResponse;
import com.okayji.feed.dto.response.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/posts")
@AllArgsConstructor
@Tag(name = "Admin Post Controller")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPostController {

    private final AdminPostService adminPostService;

    @GetMapping
    @Operation(summary = "List posts under review", description = "Returns a paginated list of posts with UNDER_REVIEW status")
    ApiResponse<Page<PostResponse>> getUnderReviewPosts(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.<Page<PostResponse>>builder()
                .success(true)
                .data(adminPostService.getUnderReviewPosts(page, size))
                .build();
    }

    @PutMapping("/{postId}/approve")
    @Operation(summary = "Approve a post", description = "Sets post status to PUBLISHED. Post must be UNDER_REVIEW.")
    ApiResponse<PostResponse> approvePost(@PathVariable String postId) {
        return ApiResponse.<PostResponse>builder()
                .success(true)
                .message("Post approved")
                .data(adminPostService.approvePost(postId))
                .build();
    }

    @PutMapping("/{postId}/reject")
    @Operation(summary = "Reject a post", description = "Sets post status to REJECTED. Post must be UNDER_REVIEW.")
    ApiResponse<PostResponse> rejectPost(@PathVariable String postId) {
        return ApiResponse.<PostResponse>builder()
                .success(true)
                .message("Post rejected")
                .data(adminPostService.rejectPost(postId))
                .build();
    }
}
