package com.okayji.feed.service;

import com.okayji.feed.dto.response.FriendReqResponse;
import com.okayji.identity.dto.response.ProfileBasicResponse;

import java.util.List;

public interface FriendService {
    void createFriendRequest(String fromUserId, String toUserIdOrUsername);
    void acceptFriendRequest(String friendRequestId);
    void deleteFriendRequest(String friendRequestId);
    void unfriend(String fromUserId, String anotherUserIdOrUsername);
    List<ProfileBasicResponse> getFriends(String userId);
    List<FriendReqResponse> getFriendRequestSent(String userId);
    List<FriendReqResponse> getFriendRequestReceived(String userId);
}
