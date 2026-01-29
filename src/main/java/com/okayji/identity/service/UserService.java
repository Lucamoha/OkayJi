package com.okayji.identity.service;

import com.okayji.identity.dto.request.UserCreationRequest;
import com.okayji.identity.dto.response.UserResponse;

public interface UserService {
    UserResponse findById(String id);
    UserResponse create(UserCreationRequest userCreationRequest);
    void delete(String userId);
}
