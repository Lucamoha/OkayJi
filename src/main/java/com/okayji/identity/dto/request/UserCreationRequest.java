package com.okayji.identity.dto.request;


import com.okayji.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, max = 15, message = "USERNAME_INVALID")
    String username;
    @Email(message = "EMAIL_INVALID")
    String email;
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String fullName;
    LocalDate birthday;
    Gender gender;
}
