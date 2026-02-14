package com.okayji.file.controller;

import com.okayji.common.ApiResponse;
import com.okayji.file.dto.request.PresignedUrlRequest;
import com.okayji.file.dto.response.PresignedUrlResponse;
import com.okayji.file.service.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/presigned-url")
    public ApiResponse<PresignedUrlResponse> generatePresignedUrl(
            @Valid @RequestBody PresignedUrlRequest request) {
        return ApiResponse.<PresignedUrlResponse>builder()
                .success(true)
                .data(s3Service.generatePresignedUrl(request))
                .build();
    }
}
