package com.okayji.identity.service;

import com.okayji.identity.dto.request.UserChangePasswordRequest;
import com.okayji.identity.dto.request.UserChangeUsernameRequest;
import com.okayji.identity.dto.request.UserCreationRequest;
import com.okayji.identity.dto.response.UserResponse;

public interface UserService {
    UserResponse findById(String id);
    UserResponse create(UserCreationRequest userCreationRequest);
    void changePassword(String userId, UserChangePasswordRequest request);
    void changeUsername(String userId, UserChangeUsernameRequest request);
    void delete(String userId);
}
