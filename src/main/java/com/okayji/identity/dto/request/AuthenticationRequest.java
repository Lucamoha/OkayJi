package com.okayji.identity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class AuthenticationRequest {
    @NotBlank(message = "Username must not be blank")
    String username;
    @NotBlank(message = "Password must not be blank")
    String password;
    boolean rememberMe;
}
