package com.okayji.identity.service;

import com.okayji.identity.dto.request.ProfileUpdateRequest;
import com.okayji.identity.dto.response.ProfileResponse;

public interface ProfileService {
    ProfileResponse getUserProfile(String viewerId, String userIdOrUsername);
    ProfileResponse updateUserProfile(String userId, ProfileUpdateRequest profileUpdateRequest);
}
