package com.okayji.admin.service;

import com.okayji.feed.dto.response.PostResponse;
import org.springframework.data.domain.Page;

public interface AdminPostService {
    Page<PostResponse> getUnderReviewPosts(int page, int size);
    PostResponse approvePost(String postId);
    PostResponse rejectPost(String postId);
}
