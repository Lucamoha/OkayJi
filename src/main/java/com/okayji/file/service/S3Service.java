package com.okayji.file.service;

import com.okayji.file.dto.request.PresignedUrlRequest;
import com.okayji.file.dto.response.PresignedUrlResponse;

public interface S3Service {
    PresignedUrlResponse generatePresignedUrl(PresignedUrlRequest request);
}
