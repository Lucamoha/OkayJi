package com.okayji.feed.service.impl;

import com.okayji.enums.PostStatus;
import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.feed.dto.request.PostCreationRequest;
import com.okayji.feed.dto.response.PostResponse;
import com.okayji.feed.entity.Post;
import com.okayji.feed.repository.CommentRepository;
import com.okayji.feed.repository.ReactionRepository;
import com.okayji.identity.entity.User;
import com.okayji.feed.repository.PostRepository;
import com.okayji.feed.service.PostService;
import com.okayji.mapper.PostMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;

    @Override
    public PostResponse getPostById(String id) {
        User user = getCurrentUser();

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));

        if (post.getStatus() != PostStatus.PUBLISHED
                && !post.getUser().getId().equals(user.getId()))
            throw new AppException(AppError.POST_NOT_FOUND);

        long reactionsCount = reactionRepository.countByPost_Id(post.getId());
        long commentsCount = commentRepository.countByPost_Id(post.getId());
        boolean liked = reactionRepository.existsByPostIdAndUserId(post.getId(), user.getId());

        return postMapper.toPostResponse(post, liked, reactionsCount, commentsCount);
    }

    @Override
    public PostResponse createPost(PostCreationRequest postCreationRequest) {
        User user = getCurrentUser();

        Post post = postMapper.toPost(postCreationRequest, user);
        postRepository.save(post);
        return postMapper.toPostResponse(post, false, 0, 0);
    }

    @Override
    public void deletePostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));

        User user = getCurrentUser();

        if (!user.getId().equals(post.getUser().getId()))
            throw new AppException(AppError.UNAUTHORIZED);

        postRepository.delete(post);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
