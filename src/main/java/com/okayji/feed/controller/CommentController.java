package com.okayji.feed.controller;

import com.okayji.common.ApiResponse;
import com.okayji.feed.dto.request.CommentCreationRequest;
import com.okayji.feed.dto.request.CommentUpdateRequest;
import com.okayji.feed.dto.response.CommentResponse;
import com.okayji.feed.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    ApiResponse<CommentResponse> createComment(@Valid @RequestBody CommentCreationRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .success(true)
                .data(commentService.createComment(request))
                .build();
    }

    @PutMapping
    ApiResponse<CommentResponse> updateComment(@Valid @RequestBody CommentUpdateRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .success(true)
                .data(commentService.updateComment(request))
                .build();
    }

    @DeleteMapping("/{commentId}")
    ApiResponse<?> deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.builder()
                .success(true)
                .build();
    }
}
