package com.okayji.feed.controller;

import com.okayji.common.ApiResponse;
import com.okayji.feed.dto.request.PostCreationRequest;
import com.okayji.feed.dto.response.CommentResponse;
import com.okayji.feed.dto.response.PostResponse;
import com.okayji.feed.service.CommentService;
import com.okayji.feed.service.PostService;
import com.okayji.feed.service.ReactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private PostService postService;
    private ReactionService reactionService;
    private CommentService commentService;

    @GetMapping("/{postId}")
    ApiResponse<PostResponse> getPost(@PathVariable String postId) {
        return ApiResponse.<PostResponse>builder()
                .success(true)
                .message("Get post success")
                .data(postService.getPostById(postId))
                .build();
    }

    @GetMapping("/{postId}/comments")
    ApiResponse<Page<CommentResponse>> getCommentsByPost(
            @PathVariable String postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.<Page<CommentResponse>>builder()
                .success(true)
                .data(commentService.getCommentsByPostId(postId, page, size))
                .build();
    }

    @PostMapping
    ApiResponse<PostResponse> createPost(@Valid @RequestBody PostCreationRequest postCreationRequest) {
        return ApiResponse.<PostResponse>builder()
                .success(true)
                .message("Create post success")
                .data(postService.createPost(postCreationRequest))
                .build();
    }

    @DeleteMapping("/{postId}")
    ApiResponse<?> deletePost(@PathVariable String postId) {
        postService.deletePostById(postId);
        return ApiResponse.builder()
                .success(true)
                .build();
    }

    @PostMapping("/{postId}/like")
    ApiResponse<?> reactPost(@PathVariable String postId) {
        reactionService.like(postId);
        return ApiResponse.builder()
                .success(true)
                .build();
    }

    @PostMapping("{postId}/unlike")
    ApiResponse<?> unReactPost(@PathVariable String postId) {
        reactionService.unlike(postId);
        return ApiResponse.builder()
                .success(true)
                .build();
    }
}
