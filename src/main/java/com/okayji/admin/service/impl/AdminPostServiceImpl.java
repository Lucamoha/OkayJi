package com.okayji.admin.service.impl;

import com.okayji.admin.service.AdminPostService;
import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.feed.dto.response.PostResponse;
import com.okayji.feed.entity.Post;
import com.okayji.feed.entity.PostStatus;
import com.okayji.feed.repository.PostRepository;
import com.okayji.mapper.PostMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminPostServiceImpl implements AdminPostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public Page<PostResponse> getUnderReviewPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepository.findByStatus(PostStatus.UNDER_REVIEW, pageable)
                .map(postMapper::toPostResponse);
    }

    @Override
    public PostResponse approvePost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));

        if (post.getStatus() != PostStatus.UNDER_REVIEW)
            throw new AppException(AppError.POST_NOT_UNDER_REVIEW);

        post.setStatus(PostStatus.PUBLISHED);
        postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    @Override
    public PostResponse rejectPost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));

        if (post.getStatus() != PostStatus.UNDER_REVIEW)
            throw new AppException(AppError.POST_NOT_UNDER_REVIEW);

        post.setStatus(PostStatus.REJECTED);
        postRepository.save(post);
        return postMapper.toPostResponse(post);
    }
}
