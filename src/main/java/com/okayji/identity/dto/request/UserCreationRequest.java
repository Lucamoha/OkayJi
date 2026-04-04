package com.okayji.identity.dto.request;


import com.okayji.identity.entity.Gender;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 15, message = "Username must be from 3 to 15 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    String username;

    @Email(message = "Email invalid")
    String email;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password;

    @NotBlank(message = "Full name is required")
    String fullName;

    @NotNull(message = "Birthday (yyyy-mm-dd) is required")
    @Past(message = "Birthday (yyyy-mm-dd) must be in the past")
    LocalDate birthday;

    @NotNull(message = "Gender is required")
    Gender gender;
}
