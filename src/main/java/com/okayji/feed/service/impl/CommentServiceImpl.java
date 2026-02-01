package com.okayji.feed.service.impl;

import com.okayji.common.PageResponse;
import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.feed.dto.request.CommentCreationRequest;
import com.okayji.feed.dto.request.CommentUpdateRequest;
import com.okayji.feed.dto.response.CommentResponse;
import com.okayji.feed.entity.Comment;
import com.okayji.feed.entity.Post;
import com.okayji.feed.repository.CommentRepository;
import com.okayji.feed.repository.PostRepository;
import com.okayji.feed.service.CommentService;
import com.okayji.identity.entity.User;
import com.okayji.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;

    @Override
    public CommentResponse createComment(CommentCreationRequest request) {
        String postId = request.getPostId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));

        User user = getCurrentUser();

        Comment comment = commentMapper.toComment(request);
        comment.setUser(user);
        comment.setPost(post);
        commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    @Override
    public CommentResponse updateComment(CommentUpdateRequest request) {
        String commentId = request.getId();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(AppError.COMMENT_NOT_FOUND));

        User user = getCurrentUser();

        if (!comment.getUser().getId().equals(user.getId()))
            throw new AppException(AppError.UNAUTHORIZED);

        commentMapper.updateComment(comment, request);
        commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    @Override
    public void deleteComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(AppError.COMMENT_NOT_FOUND));

        User user = getCurrentUser();

        if (!comment.getUser().getId().equals(user.getId()))
            throw new AppException(AppError.UNAUTHORIZED);

        commentRepository.delete(comment);
    }

    @Override
    public PageResponse<CommentResponse> getListCommentInPost(String postId, int page, int size, String sortBy, String sortType) {
        Sort sort;

        if (sortType.equals("asc"))
            sort = Sort.by(Sort.Direction.ASC, sortBy);
        else
            sort = Sort.by(Sort.Direction.DESC, sortBy);

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<CommentResponse> pageable = commentRepository.findAll(pageRequest).map(comment -> commentMapper.toCommentResponse(comment));

        return PageResponse.<CommentResponse>builder()
                .page(page)
                .size(size)
                .totalElements(pageable.getTotalElements())
                .totalPages(pageable.getTotalPages())
                .sortBy(sortBy)
                .sortType(sortType)
                .results(pageable.stream().toList())
                .build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
