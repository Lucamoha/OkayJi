package com.okayji.feed.repository;

import com.okayji.feed.entity.FriendRequest;
import com.okayji.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest,String> {
    boolean existsBySenderIdAndReceiverId(String senderId, String receiverId);

    FriendRequest findBySenderAndReceiver(User sender, User receiver);

    List<FriendRequest> findBySenderId(String senderId);

    List<FriendRequest> findByReceiverId(String receiverId);
}
