package com.okayji.feed.service.impl;

import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.feed.dto.request.PostCreationRequest;
import com.okayji.feed.dto.response.PostResponse;
import com.okayji.feed.entity.Comment;
import com.okayji.feed.entity.Post;
import com.okayji.feed.entity.Reaction;
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

import java.util.List;


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

        List<Reaction> reactions = reactionRepository.findByPostId(post.getId());
        List<Comment> comments = commentRepository.findByPostId(post.getId());
        boolean liked = false;

        for (Reaction reaction : reactions)
            if (reaction.getUser().getId().equals(user.getId()))
                liked = true;

        return postMapper.toPostResponse(post, liked, reactions.size(), comments.size());
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
