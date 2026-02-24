package com.okayji.feed.service.impl;

import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.feed.entity.Post;
import com.okayji.feed.entity.Reaction;
import com.okayji.feed.repository.PostRepository;
import com.okayji.feed.repository.ReactionRepository;
import com.okayji.feed.service.ReactionService;
import com.okayji.identity.entity.User;
import com.okayji.identity.repository.UserRepository;
import com.okayji.notification.service.NotificationService;
import com.okayji.notification.service.NotificationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Override
    public void like(String userId, String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        Reaction reaction = new Reaction();
        reaction.setUser(user);
        reaction.setPost(post);

        try {
            reactionRepository.save(reaction);

            // Ping noti to other user
            notificationService.sendNotification(NotificationFactory.likePost(post, user));
        }
        catch (DataIntegrityViolationException ignored) {}
    }

    @Override
    public void unlike(String userId, String postId) {
        if (reactionRepository.existsByPostIdAndUserId(postId, userId))
            reactionRepository.deleteByPostIdAndUserId(postId, userId);
    }
}
