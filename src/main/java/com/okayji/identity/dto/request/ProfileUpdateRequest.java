package com.okayji.identity.dto.request;

import com.okayji.identity.entity.Gender;
import com.okayji.identity.entity.ProfileVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileUpdateRequest {
    @NotBlank(message = "Full name is required")
    String fullName;

    String bio;

    @NotNull(message = "Gender is required")
    Gender gender;

    @NotNull(message = "Birthday (yyyy-mm-dd) is required")
    @Past(message = "Birthday (yyyy-mm-dd) must be in the past")
    LocalDate birthday;

    String avatarUrl;

    String coverImageUrl;

    @NotNull(message = "Profile Visibility is required")
    ProfileVisibility visibility;
}
