package com.okayji.mapper;

import com.okayji.identity.dto.request.UserCreationRequest;
import com.okayji.identity.dto.response.UserProfileResponse;
import com.okayji.identity.dto.response.UserResponse;
import com.okayji.identity.entity.User;
import com.okayji.identity.entity.UserPost;
import com.okayji.identity.entity.UserProfile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest user);
    UserResponse toUserResponse(User user);
    UserProfileResponse toUserProfileResponse(UserProfile userProfile, List<UserPost> userPosts);
}
