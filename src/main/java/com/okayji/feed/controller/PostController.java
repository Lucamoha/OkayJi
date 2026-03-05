package com.okayji.feed.controller;

import com.okayji.common.ApiResponse;
import com.okayji.feed.dto.request.PostCreationRequest;
import com.okayji.feed.dto.request.PostUpdateRequest;
import com.okayji.feed.dto.response.CommentResponse;
import com.okayji.feed.dto.response.PostResponse;
import com.okayji.feed.service.CommentService;
import com.okayji.feed.service.PostService;
import com.okayji.feed.service.ReactionService;
import com.okayji.identity.entity.User;
import com.okayji.utils.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
@Tag(name = "Post Controller")
public class PostController {

    private PostService postService;
    private ReactionService reactionService;
    private CommentService commentService;

    @GetMapping("/{postId}")
    @Operation(summary = "Get post by postId")
    @PreAuthorize("@permissionCheck.canViewPost(#currentUser.id, #postId)")
    ApiResponse<PostResponse> getPost(@PathVariable String postId,
                                      @CurrentUser User currentUser) {
        return ApiResponse.<PostResponse>builder()
                .success(true)
                .message("Get post success")
                .data(postService.getPostById(currentUser.getId(), postId))
                .build();
    }

    @PutMapping("/{postId}")
    @Operation(summary = "Update post by postId")
    @PreAuthorize("@permissionCheck.canAlterPost(#currentUser.id, #postId)")
    ApiResponse<PostResponse> updatePost(@PathVariable String postId,
                                         @Valid @RequestBody PostUpdateRequest postUpdateRequest,
                                         @CurrentUser User currentUser) {
        return ApiResponse.<PostResponse>builder()
                .success(true)
                .message("Update post success")
                .data(postService.updatePost(postId, postUpdateRequest))
                .build();
    }

    @GetMapping("/{postId}/comments")
    @Operation(summary = "Get comments in post by postId")
    @PreAuthorize("@permissionCheck.canViewPost(#currentUser.id, #postId)")
    ApiResponse<Page<CommentResponse>> getCommentsByPost(@PathVariable String postId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size,
                                                         @CurrentUser User currentUser
    ) {
        return ApiResponse.<Page<CommentResponse>>builder()
                .success(true)
                .data(commentService.getCommentsByPostId(postId, page, size))
                .build();
    }

    @PostMapping
    @Operation(summary = "Create post")
    ApiResponse<PostResponse> createPost(@Valid @RequestBody PostCreationRequest postCreationRequest,
                                         @CurrentUser User currentUser) {
        return ApiResponse.<PostResponse>builder()
                .success(true)
                .message("Create post success")
                .data(postService.createPost(currentUser.getId(), postCreationRequest))
                .build();
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "Delete post by postId")
    @PreAuthorize("@permissionCheck.canAlterPost(#currentUser.id, #postId)")
    ApiResponse<?> deletePost(@PathVariable String postId,
                              @CurrentUser User currentUser) {
        postService.deletePostById(postId);
        return ApiResponse.builder()
                .success(true)
                .build();
    }

    @PostMapping("/{postId}/like")
    @Operation(summary = "React post by postId")
    @PreAuthorize("@permissionCheck.canViewPost(#currentUser.id, #postId)")
    ApiResponse<?> reactPost(@PathVariable String postId,
                             @CurrentUser User currentUser) {
        reactionService.like(currentUser.getId(), postId);
        return ApiResponse.builder()
                .success(true)
                .build();
    }

    @PostMapping("{postId}/unlike")
    @Operation(summary = "Unreact post by postId")
    @PreAuthorize("@permissionCheck.canViewPost(#currentUser.id, #postId)")
    ApiResponse<?> unReactPost(@PathVariable String postId,
                               @CurrentUser User currentUser) {
        reactionService.unlike(currentUser.getId(), postId);
        return ApiResponse.builder()
                .success(true)
                .build();
    }
}
