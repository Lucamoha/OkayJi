package com.okayji.feed.controller;

import com.okayji.common.ApiResponse;
import com.okayji.feed.dto.request.PostCreationRequest;
import com.okayji.feed.dto.response.PostResponse;
import com.okayji.feed.service.PostService;
import com.okayji.feed.service.ReactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@AllArgsConstructor
public class PostController {

    private PostService postService;
    private ReactionService reactionService;

    @GetMapping("/{postId}")
    ApiResponse<PostResponse> getPost(@PathVariable String postId) {
        return ApiResponse.<PostResponse>builder()
                .success(true)
                .message("Get post success")
                .data(postService.getPostById(postId))
                .build();
    }

    @PostMapping("/create")
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

    @PostMapping("/like/{postId}")
    ApiResponse<?> reactPost(@PathVariable String postId) {
        reactionService.like(postId);
        return ApiResponse.builder()
                .success(true)
                .build();
    }

    @PostMapping("/unlike/{postId}")
    ApiResponse<?> unReactPost(@PathVariable String postId) {
        reactionService.unlike(postId);
        return ApiResponse.builder()
                .success(true)
                .build();
    }
}
