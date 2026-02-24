package com.okayji.notification.service;

import com.okayji.notification.dto.NotificationResponse;
import com.okayji.notification.entity.Notification;
import org.springframework.data.domain.Page;

public interface NotificationService {
    void sendNotification(Notification notification);
    long unreadCount(String userId);
    void readNotification(Long notificationId);
    void readAll(String userId);
    Page<NotificationResponse> findByUserId(String userId, int page, int size);
}
