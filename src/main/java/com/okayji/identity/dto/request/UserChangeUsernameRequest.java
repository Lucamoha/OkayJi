package com.okayji.identity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class UserChangeUsernameRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 15, message = "Username must be from 3 to 15 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    String newUsername;

    @NotBlank(message = "Password is required")
    String password;
}
