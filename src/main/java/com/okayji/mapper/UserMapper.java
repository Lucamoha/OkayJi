package com.okayji.mapper;

import com.okayji.identity.dto.request.UserCreationRequest;
import com.okayji.identity.dto.response.UserResponse;
import com.okayji.identity.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest user);
    UserResponse toUserResponse(User user);
}
