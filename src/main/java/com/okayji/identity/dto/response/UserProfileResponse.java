package com.okayji.identity.dto.response;

import com.okayji.identity.entity.UserPost;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public class UserProfileResponse {
    String userId;
    String fullName;
    String bio;
    LocalDate birthday;
    String avatarUrl;
    String coverImageUrl;
    List<UserPost> userPosts;
}
