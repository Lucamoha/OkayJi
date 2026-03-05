package com.okayji.utils;

import com.okayji.chat.repository.ChatMemberRepository;
import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.feed.entity.Comment;
import com.okayji.feed.entity.FriendRequest;
import com.okayji.feed.entity.Post;
import com.okayji.feed.repository.CommentRepository;
import com.okayji.feed.repository.FriendRepository;
import com.okayji.feed.repository.FriendRequestRepository;
import com.okayji.feed.repository.PostRepository;
import com.okayji.identity.entity.Profile;
import com.okayji.identity.entity.User;
import com.okayji.identity.repository.UserRepository;
import com.okayji.notification.entity.Notification;
import com.okayji.notification.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.okayji.feed.entity.PostStatus.PUBLISHED;
import static com.okayji.identity.entity.ProfileVisibility.PUBLIC;

@Service("permissionCheck")
@AllArgsConstructor
public class PermissionCheck {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final NotificationRepository notificationRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public boolean canViewPost(String viewerId, String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));
        User author = post.getUser();

        boolean isOwner = author.getId().equals(viewerId);
        boolean canNonOwnerSee = (author.getProfile().getVisibility() == PUBLIC || isFriend(viewerId, author.getId()))
                && post.getStatus() == PUBLISHED;
        return isOwner || canNonOwnerSee;
    }

    public boolean canViewProfilePosts(String viewerId, String userIdOrUsername) {
        Profile profile = userRepository.findUserByIdOrUsername(userIdOrUsername, userIdOrUsername)
                .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND))
                .getProfile();

        return viewerId.equals(profile.getUserId())
                || profile.getVisibility().equals(PUBLIC)
                || isFriend(viewerId, profile.getUserId());
    }

    public boolean isFriend(String userId, String friendId) {
        return userId.compareTo(friendId) < 0
                ? friendRepository.existsByUserLow_IdAndUserHigh_Id(userId, friendId)
                : friendRepository.existsByUserLow_IdAndUserHigh_Id(friendId, userId);
    }

    public boolean canReadNotification(String userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(AppError.NOTI_NOT_FOUND));
        return notification.getUser().getId().equals(userId);
    }

    public boolean canAccessChat(String userId, String chatId) {
        return chatMemberRepository.existsByChat_IdAndMember_Id(chatId, userId);
    }

    public boolean canAlterPost(String userId, String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));

        return post.getUser().getId().equals(userId);
    }

    public boolean canAlterComment(String userId, String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(AppError.COMMENT_NOT_FOUND));

        return comment.getUser().getId().equals(userId);
    }

    /**
     *
     * @param userId
     * @param friendRequestId
     * @param action "ACCEPT", "DECLINE", "CANCEL"
     */
    public boolean canAlterFriendRequest(String userId,
                                         String friendRequestId,
                                         String action) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new AppException(AppError.FRIEND_REQUEST_NOT_FOUND));

        return switch (action) {
            case "ACCEPT", "DECLINE" -> friendRequest.getReceiver().getId().equals(userId);
            case "CANCEL" -> friendRequest.getSender().getId().equals(userId);
            default -> false;
        };
    }
}
