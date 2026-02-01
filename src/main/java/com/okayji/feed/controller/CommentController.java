package com.okayji.feed.controller;

import com.okayji.common.ApiResponse;
import com.okayji.feed.dto.request.CommentCreationRequest;
import com.okayji.feed.dto.response.CommentResponse;
import com.okayji.feed.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    ApiResponse<CommentResponse> createPost(@Valid @RequestBody CommentCreationRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .success(true)
                .message("Comment success")
                .data(commentService.createComment(request))
                .build();
    }
}
