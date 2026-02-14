package com.okayji.file.service.impl;

import com.okayji.file.dto.request.PresignedUrlRequest;
import com.okayji.file.dto.response.PresignedUrlResponse;
import com.okayji.file.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "S3-SERVICE")
public class S3ServiceImpl implements S3Service {
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    @Value("${presigned-url.expiration-minutes}")
    private Integer expirationMinutes;

    @Override
    public PresignedUrlResponse generatePresignedUrl(PresignedUrlRequest request) {
        String fileExtension = getFileExtension(request.getFileName());
        String uniqueKey = String.format("images/%s/%s.%s",
                LocalDate.now(),
                UUID.randomUUID(),
                fileExtension);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueKey)
                .contentType(request.getFileType())
                .build();

        // Generate presigned URL
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(expirationMinutes))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        String presignedUrl = presignedRequest.url().toString();
        String publicUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName, region, uniqueKey);

        log.info("Generated presigned URL for key: {}", uniqueKey);

        return PresignedUrlResponse.builder()
                .presignedUrl(presignedUrl)
                .fileKey(uniqueKey)
                .publicUrl(publicUrl)
                .expiresIn(expirationMinutes * 60)
                .build();
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }
}
