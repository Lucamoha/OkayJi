package com.okayji.file.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PresignedUrlRequest {
    @NotBlank(message = "Filename must not be blank")
    String fileName;
    @NotBlank(message = "File type must not be blank")
    @Pattern(regexp = "image/(jpeg|png|gif|webp)",
            message = "Only image files are allowed")
    String fileType;
}
