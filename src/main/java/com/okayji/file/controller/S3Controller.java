package com.okayji.file.controller;

import com.okayji.common.ApiResponse;
import com.okayji.file.dto.request.AbortUploadRequest;
import com.okayji.file.dto.request.MultipartUploadCompleteRequest;
import com.okayji.file.dto.request.PresignedUrlRequest;
import com.okayji.file.dto.response.MultipartUploadInitResponse;
import com.okayji.file.dto.response.PresignedUrlResponse;
import com.okayji.file.service.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/presigned-url")
    ApiResponse<PresignedUrlResponse> generatePresignedUrl(
            @Valid @RequestBody PresignedUrlRequest request) {
        return ApiResponse.<PresignedUrlResponse>builder()
                .success(true)
                .data(s3Service.generatePresignedUrl(request))
                .build();
    }

    @PostMapping("/multipart/initiate")
    ApiResponse<MultipartUploadInitResponse> initiateUpload(
            @Valid @RequestBody PresignedUrlRequest request) {

        return ApiResponse.<MultipartUploadInitResponse>builder()
                .success(true)
                .data(s3Service.initMultipartUpload(request))
                .build();
    }

    @PostMapping("/multipart/complete")
    ApiResponse<Map> completeUpload(@Valid @RequestBody MultipartUploadCompleteRequest request) {
        String location = s3Service.completeMultipartUpload(request);
        return ApiResponse.<Map>builder()
                .success(true)
                .data(Map.of("location", location))
                .build();
    }

    @PostMapping("/multipart/abort")
    ApiResponse<?> abortUpload(@Valid @RequestBody AbortUploadRequest request) {
        s3Service.abortMultipartUpload(request.getFileKey(), request.getUploadId());
        return ApiResponse.builder()
                .success(true)
                .build();
    }
}
