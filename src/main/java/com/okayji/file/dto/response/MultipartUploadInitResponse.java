package com.okayji.file.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class MultipartUploadInitResponse {
    String uploadId;
    String fileKey;
    List<PartPresignedUrl> parts;
    Integer partSize; // bytes
    String publicUrl;
}
