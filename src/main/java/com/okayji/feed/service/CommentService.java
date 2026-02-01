package com.okayji.feed.service;

import com.okayji.feed.dto.request.CommentCreationRequest;
import com.okayji.feed.dto.response.CommentResponse;

public interface CommentService {
    CommentResponse createComment(CommentCreationRequest request);
    void deleteComment(String commentId);
}
