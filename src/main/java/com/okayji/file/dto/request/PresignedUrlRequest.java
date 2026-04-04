package com.okayji.file.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PresignedUrlRequest {
    @NotBlank(message = "Filename is required")
    String fileName;

    @NotBlank(message = "File type is required")
    String fileType; // content-type

    @NotNull(message = "File size (bytes) is required")
    Long fileSize; // bytes
}
