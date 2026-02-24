package com.okayji.feed.service.impl;

import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.feed.dto.request.PostCreationRequest;
import com.okayji.feed.dto.request.PostUpdateRequest;
import com.okayji.feed.dto.response.PostResponse;
import com.okayji.feed.entity.Post;
import com.okayji.feed.entity.PostMedia;
import com.okayji.feed.repository.CommentRepository;
import com.okayji.feed.repository.ReactionRepository;
import com.okayji.identity.entity.User;
import com.okayji.feed.repository.PostRepository;
import com.okayji.feed.service.PostService;
import com.okayji.identity.repository.UserRepository;
import com.okayji.mapper.PostMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Override
    public PostResponse getPostById(String viewerId, String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));

        return postMapper.toPostResponse(post,
                reactionRepository.existsByPostIdAndUserId(post.getId(), viewerId),
                reactionRepository.countByPost_Id(post.getId()),
                commentRepository.countByPost_Id(post.getId()));
    }

    @Override
    @Transactional(rollbackOn = AppException.class)
    public PostResponse createPost(String userId, PostCreationRequest postCreationRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        Post post = postMapper.toPost(postCreationRequest, user);

        postCreationRequest.getMedia().forEach(media -> {
            PostMedia postMedia = PostMedia.builder()
                    .post(post)
                    .type(media.getType())
                    .mediaUrl(media.getMediaUrl())
                    .build();
            post.getPostMedia().add(postMedia);
        });

        postRepository.saveAndFlush(post);
        entityManager.refresh(post);

        return postMapper.toPostResponse(post);
    }

    @Override
    public PostResponse updatePost(String postId, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));

        postMapper.updatePost(post, postUpdateRequest);
        postRepository.save(post);

        return postMapper.toPostResponse(post);
    }

    @Override
    public void deletePostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));

        postRepository.delete(post);
    }

    @Override
    public Page<PostResponse> getPostsByUser(String viewerId, String userIdOrUsername, int page, int size) {
        userRepository.findUserByIdOrUsername(userIdOrUsername, userIdOrUsername)
                .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        Pageable pageable = PageRequest
                .of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return postRepository
                .findByUser_Id(userIdOrUsername, pageable)
                .map(post -> postMapper.toPostResponse(post,
                        reactionRepository.existsByPostIdAndUserId(post.getId(), viewerId),
                        reactionRepository.countByPost_Id(post.getId()),
                        commentRepository.countByPost_Id(post.getId()))
                );
    }
}
