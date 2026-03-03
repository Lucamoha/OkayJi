package com.okayji.feed.controller;

import com.okayji.common.ApiResponse;
import com.okayji.feed.dto.response.FriendReqResponse;
import com.okayji.feed.service.FriendService;
import com.okayji.identity.dto.response.ProfileBasicResponse;
import com.okayji.identity.entity.User;
import com.okayji.utils.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
@AllArgsConstructor
@Tag(name = "Friend Controller")
public class FriendController {

    private final FriendService friendService;

    @GetMapping
    @Operation(summary = "Get friend list")
    public ApiResponse<List<ProfileBasicResponse>> getFriends(@CurrentUser User currentUser) {
        return ApiResponse.<List<ProfileBasicResponse>>builder()
                .success(true)
                .data(friendService.getFriends(currentUser.getId()))
                .build();
    }

    @GetMapping("/received")
    @Operation(summary = "Get friend requests received")
    public ApiResponse<List<FriendReqResponse>> getFriendRequestsReceived(@CurrentUser User currentUser) {
        return ApiResponse.<List<FriendReqResponse>>builder()
                .success(true)
                .data(friendService.getFriendRequestReceived(currentUser.getId()))
                .build();
    }

    @GetMapping("/sent")
    @Operation(summary = "Get friend requests sent")
    public ApiResponse<List<FriendReqResponse>> getFriendRequestsSent(@CurrentUser User currentUser) {
        return ApiResponse.<List<FriendReqResponse>>builder()
                .success(true)
                .data(friendService.getFriendRequestSent(currentUser.getId()))
                .build();
    }

    @PostMapping("/request/{toUserIdOrUsername}")
    @Operation(summary = "Send friend request to another")
    public ApiResponse<?> sendFriendRequest(@PathVariable String toUserIdOrUsername,
                                            @CurrentUser User currentUser){
        friendService.createFriendRequest(currentUser.getId(), toUserIdOrUsername);
        return ApiResponse.builder()
                .success(true)
                .message("Friend request sent")
                .build();
    }

    @PostMapping("/accept/{friendRequestId}")
    @Operation(summary = "Accept friend request")
    @PreAuthorize("@permissionCheck.canAlterFriendRequest(currentUser.getId(), #friendRequestId, 'ACCEPT')")
    public ApiResponse<?> acceptFriendRequest(@PathVariable String friendRequestId,
                                              @CurrentUser User currentUser){
        friendService.acceptFriendRequest(friendRequestId);
        return ApiResponse.builder()
                .success(true)
                .message("Friend request accepted")
                .build();
    }

    @PostMapping("/decline/{friendRequestId}")
    @Operation(summary = "Decline friend request")
    @PreAuthorize("@permissionCheck.canAlterFriendRequest(currentUser.getId(), #friendRequestId, 'DECLINE')")
    public ApiResponse<?> declineFriendRequest(@PathVariable String friendRequestId,
                                               @CurrentUser User currentUser){
        friendService.deleteFriendRequest(friendRequestId);
        return ApiResponse.builder()
                .success(true)
                .message("Friend request declined")
                .build();
    }

    @PostMapping("/cancel/{friendRequestId}")
    @Operation(summary = "Cancel friend request")
    @PreAuthorize("@permissionCheck.canAlterFriendRequest(currentUser.getId(), #friendRequestId, 'CANCEL')")
    public ApiResponse<?> cancelFriendRequest(@PathVariable String friendRequestId,
                                              @CurrentUser User currentUser){
        friendService.deleteFriendRequest(friendRequestId);
        return ApiResponse.builder()
                .success(true)
                .message("Friend request cancelled")
                .build();
    }

    @PostMapping("/unfriend/{anotherUserIdOrUsername}")
    @Operation(summary = "Unfriend another by userId or username")
    public ApiResponse<?> unfriend(@PathVariable String anotherUserIdOrUsername,
                                   @CurrentUser User currentUser){
        friendService.unfriend(currentUser.getId(), anotherUserIdOrUsername);
        return ApiResponse.builder()
                .success(true)
                .message("Unfriend successfully")
                .build();
    }
}
