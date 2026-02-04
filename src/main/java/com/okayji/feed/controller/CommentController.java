package com.okayji.feed.controller;

import com.okayji.common.ApiResponse;
import com.okayji.feed.dto.request.CommentCreationRequest;
import com.okayji.feed.dto.request.CommentUpdateRequest;
import com.okayji.feed.dto.response.CommentResponse;
import com.okayji.feed.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
@Tag(name = "Comment Controller")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Create comment")
    ApiResponse<CommentResponse> createComment(@Valid @RequestBody CommentCreationRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .success(true)
                .data(commentService.createComment(request))
                .build();
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "Update comment by commentId")
    ApiResponse<CommentResponse> updateComment(@PathVariable String commentId,
                                               @Valid @RequestBody CommentUpdateRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .success(true)
                .data(commentService.updateComment(commentId, request))
                .build();
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete comment by commentId")
    ApiResponse<?> deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.builder()
                .success(true)
                .build();
    }
}
