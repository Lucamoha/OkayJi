package com.okayji.identity.service.impl;

import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.feed.entity.FriendRequest;
import com.okayji.feed.repository.FriendRepository;
import com.okayji.feed.repository.FriendRequestRepository;
import com.okayji.file.service.S3MediaTypes;
import com.okayji.file.service.S3Service;
import com.okayji.identity.dto.request.ProfileUpdateRequest;
import com.okayji.identity.dto.response.ProfileResponse;
import com.okayji.identity.entity.Profile;
import com.okayji.identity.entity.User;
import com.okayji.identity.repository.ProfileRepository;
import com.okayji.identity.repository.UserRepository;
import com.okayji.identity.service.ProfileService;
import com.okayji.mapper.FriendRequestMapper;
import com.okayji.mapper.ProfileMapper;
import com.okayji.utils.PairUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@AllArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;
    private final FriendRequestMapper friendRequestMapper;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final S3Service s3Service;

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getUserProfile(String viewerId, String userIdOrUsername) {
        User user = userRepository.findUserByIdOrUsername(userIdOrUsername, userIdOrUsername)
                .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));
        Profile profile = user.getProfile();
        ProfileResponse profileResponse = profileMapper.toProfileResponse(profile);
        if (user.getId().equals(viewerId))
            return profileResponse;

        User viewer = userRepository.findById(viewerId)
                .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        // if friend
        var pair = PairUser.canonical(user, viewer);
        if (friendRepository.existsByUserLowIdAndUserHighId(pair.getLow().getId(), pair.getHigh().getId())) {
            profileResponse.setFriend(true);
            return profileResponse;
        }

        // if friend request exist
        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(user, viewer);
        if (friendRequest == null)
            friendRequest = friendRequestRepository.findBySenderAndReceiver(viewer, user);
        profileResponse.setFriendRequest(
                Objects.nonNull(friendRequest)
                ? friendRequestMapper.toFriendReqResponse(
                        friendRequest,
                        profileMapper.toProfileBasicResponse(friendRequest.getSender().getProfile()),
                        profileMapper.toProfileBasicResponse(friendRequest.getReceiver().getProfile())
                )
                : null
        );
        return profileResponse;
    }


    @Override
    @Transactional
    public ProfileResponse updateUserProfile(String userId, ProfileUpdateRequest profileUpdateRequest) {
        validateImageUrls(profileUpdateRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        Profile profile = user.getProfile();
        profileMapper.updateProfile(profile, profileUpdateRequest);

        return profileMapper.toProfileResponse(profile);
    }

    private void validateImageUrls(ProfileUpdateRequest req) {
        if (req.getAvatarUrl() != null && !req.getAvatarUrl().isBlank()) {
            String contentType = s3Service.getContentTypeFromS3Url(req.getAvatarUrl());
            if (!S3MediaTypes.isImageType(contentType))
                throw new AppException(AppError.INVALID_INPUT_DATA);
        }

        if (req.getCoverImageUrl() != null && !req.getCoverImageUrl().isBlank()) {
            String contentType = s3Service.getContentTypeFromS3Url(req.getCoverImageUrl());
            if (!S3MediaTypes.isImageType(contentType))
                throw new AppException(AppError.INVALID_INPUT_DATA);
        }
    }
}
