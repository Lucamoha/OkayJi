package com.okayji.feed.service;

import com.okayji.common.PageResponse;
import com.okayji.feed.dto.request.CommentCreationRequest;
import com.okayji.feed.dto.request.CommentUpdateRequest;
import com.okayji.feed.dto.response.CommentResponse;

public interface CommentService {
    CommentResponse createComment(CommentCreationRequest request);
    CommentResponse updateComment(CommentUpdateRequest request);
    void deleteComment(String commentId);
    PageResponse<CommentResponse> getListCommentInPost(String postId,
                                                       int page,
                                                       int size,
                                                       String sortBy,
                                                       String sortType);
}
