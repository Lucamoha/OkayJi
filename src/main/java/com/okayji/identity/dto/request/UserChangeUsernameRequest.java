package com.okayji.identity.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class UserChangeUsernameRequest {
    @Size(min = 3, max = 15, message = "USERNAME_INVALID")
    String newUsername;
    String password;
}
