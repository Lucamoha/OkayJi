package com.okayji.file.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbortUploadRequest {
    @NotBlank(message = "File key must not be blank")
    String fileKey;
    @NotBlank(message = "Upload id must not be blank")
    String uploadId;
}
