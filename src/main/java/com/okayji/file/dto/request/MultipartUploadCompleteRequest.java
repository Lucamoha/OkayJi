package com.okayji.file.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MultipartUploadCompleteRequest {
    @NotBlank(message = "File key must not be blank")
    String fileKey;
    @NotBlank(message = "Upload id must not be blank")
    String uploadId;
    List<CompletedPartInfo> parts;
}
