package com.okayji.file.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MultipartUploadCompleteRequest {
    @NotBlank(message = "File key is required")
    String fileKey;

    @NotBlank(message = "Upload id is required")
    String uploadId;

    @NotEmpty(message = "List completed part info is required")
    List<CompletedPartInfo> parts;
}
