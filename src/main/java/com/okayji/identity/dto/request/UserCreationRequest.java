package com.okayji.identity.dto.request;


import com.okayji.identity.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, max = 15, message = "Username must be from 3 to 15 characters long")
    String username;
    @Email(message = "Email invalid")
    String email;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password;
    String fullName;
    LocalDate birthday;
    Gender gender;
}
