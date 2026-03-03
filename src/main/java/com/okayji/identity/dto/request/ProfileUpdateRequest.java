package com.okayji.identity.dto.request;

import com.okayji.identity.entity.Gender;
import com.okayji.identity.entity.ProfileVisibility;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileUpdateRequest {
    String fullName;
    String bio;
    Gender gender;
    LocalDate birthday;
    String avatarUrl;
    String coverImageUrl;
    ProfileVisibility visibility;
}
