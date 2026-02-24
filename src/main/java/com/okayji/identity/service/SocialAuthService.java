package com.okayji.identity.service;

import com.okayji.chat.repository.ChatMemberRepository;
import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.feed.entity.Comment;
import com.okayji.feed.entity.FriendRequest;
import com.okayji.feed.entity.Post;
import com.okayji.feed.repository.CommentRepository;
import com.okayji.feed.repository.FriendRequestRepository;
import com.okayji.feed.repository.PostRepository;
import com.okayji.identity.entity.User;
import com.okayji.notification.entity.Notification;
import com.okayji.notification.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("socialAuth")
@AllArgsConstructor
public class SocialAuthService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final NotificationRepository notificationRepository;

    public boolean canReadNotification(Authentication authentication, Long notificationId) {
        User user = (User) authentication.getPrincipal();
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(AppError.NOTI_NOT_FOUND));
        return notification.getUser().getId().equals(user.getId());
    }

    public boolean canAccessChat(Authentication authentication, String chatId) {
        User user = (User) authentication.getPrincipal();
        return chatMemberRepository.existsByChat_IdAndMember_Id(chatId, user.getId());
    }

    public boolean canAlterPost(Authentication authentication, String postId) {
        User user = (User) authentication.getPrincipal();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));

        return post.getUser().getId().equals(user.getId());
    }

    public boolean canAlterComment(Authentication authentication, String commentId) {
        User user = (User) authentication.getPrincipal();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(AppError.COMMENT_NOT_FOUND));

        return comment.getUser().getId().equals(user.getId());
    }

    /**
     *
     * @param authentication
     * @param friendRequestId
     * @param action "ACCEPT", "DECLINE", "CANCEL"
     */
    public boolean canAlterFriendRequest(Authentication authentication,
                                         String friendRequestId,
                                         String action) {
        User user = (User) authentication.getPrincipal();
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new AppException(AppError.FRIEND_REQUEST_NOT_FOUND));

        return switch (action) {
            case "ACCEPT", "DECLINE" -> friendRequest.getReceiver().getId().equals(user.getId());
            case "CANCEL" -> friendRequest.getSender().getId().equals(user.getId());
            default -> false;
        };
    }
}
