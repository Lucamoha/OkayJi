package com.okayji.identity.service.impl;

import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.identity.dto.response.UserProfileResponse;
import com.okayji.identity.entity.User;
import com.okayji.identity.entity.UserPost;
import com.okayji.identity.entity.UserProfile;
import com.okayji.identity.repository.UserPostRepository;
import com.okayji.identity.repository.UserProfileRepository;
import com.okayji.identity.service.UserProfileService;
import com.okayji.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserPostRepository userPostRepository;
    private final UserMapper userMapper;

    @Override
    public UserProfileResponse getUserProfile(String userId) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        List<UserPost> userPosts = userPostRepository.findByUserId(userId);

        return userMapper.toUserProfileResponse(userProfile,userPosts);
    }

    @Override
    public UserProfileResponse getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return this.getUserProfile(user.getId());
    }
}
