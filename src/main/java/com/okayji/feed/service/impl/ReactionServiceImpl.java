package com.okayji.feed.service.impl;

import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.feed.entity.Post;
import com.okayji.feed.entity.Reaction;
import com.okayji.feed.repository.PostRepository;
import com.okayji.feed.repository.ReactionRepository;
import com.okayji.feed.service.ReactionService;
import com.okayji.identity.entity.User;
import com.okayji.notification.service.NotificationService;
import com.okayji.notification.service.impl.NotificationFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void like(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));

        User user = getCurrentUser();
        if (reactionRepository.existsByPostIdAndUserId(postId, user.getId())) {
            return;
        }

        Reaction reaction = new Reaction();
        reaction.setUser(user);
        reaction.setPost(post);

        try {
            reactionRepository.saveAndFlush(reaction);
            notificationService.sendNotification(NotificationFactory.likePost(post, user));
        }
        catch (DataIntegrityViolationException ignored) {}
    }

    @Override
    @Transactional
    public void unlike(String postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));

        User user = getCurrentUser();
        if (reactionRepository.existsByPostIdAndUserId(postId, user.getId())) {
            reactionRepository.deleteByPostIdAndUserId(postId, user.getId());
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
