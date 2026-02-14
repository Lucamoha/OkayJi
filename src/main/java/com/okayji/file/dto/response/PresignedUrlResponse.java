package com.okayji.file.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class PresignedUrlResponse {
    String presignedUrl;
    String fileKey;
    String publicUrl;
    Integer expiresIn;
}
