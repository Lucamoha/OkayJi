package com.okayji.identity.service;

import com.okayji.identity.dto.response.UserProfileResponse;

public interface UserProfileService {
    UserProfileResponse getUserProfile(String userId);
    UserProfileResponse getMyProfile();
}
