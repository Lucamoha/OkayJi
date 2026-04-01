package com.okayji.feed.repository;

import com.okayji.feed.entity.Friend;
import com.okayji.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend,String> {
    boolean existsByUserLowIdAndUserHighId(String userLowId, String userHighId);

    Friend findByUserLowAndUserHigh(User userLow, User userHigh);

    List<Friend> findByUserLowIdOrUserHighId(String userLowId, String userHighId);
}
