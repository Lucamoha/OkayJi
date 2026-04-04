package com.okayji.file.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompletedPartInfo {
    @NotNull(message = "Part number is required")
    Integer partNumber;

    @NotBlank(message = "Etag id is required")
    String etag; // s3 response
}
