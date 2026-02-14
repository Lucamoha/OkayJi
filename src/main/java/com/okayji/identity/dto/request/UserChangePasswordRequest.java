package com.okayji.identity.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class UserChangePasswordRequest {
    String oldPassword;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String newPassword;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String newPasswordConfirm;
}
