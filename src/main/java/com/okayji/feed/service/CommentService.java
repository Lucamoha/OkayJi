package com.okayji.feed.service;

import com.okayji.feed.dto.request.CommentCreationRequest;
import com.okayji.feed.dto.request.CommentUpdateRequest;
import com.okayji.feed.dto.response.CommentResponse;
import org.springframework.data.domain.Page;

public interface CommentService {
    CommentResponse createComment(CommentCreationRequest request);
    CommentResponse updateComment(CommentUpdateRequest request);
    void deleteComment(String commentId);
    Page<CommentResponse> getCommentsByPostId(String postId, int page, int size);
}
