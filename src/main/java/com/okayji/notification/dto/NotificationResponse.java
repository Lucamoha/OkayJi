package com.okayji.notification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.okayji.notification.entity.NotificationType;
import lombok.Builder;

import java.time.Instant;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotificationResponse(
        Long id,
        String userId,
        String payload,
        NotificationType type,
        boolean read,
        Instant createdAt
) {}
