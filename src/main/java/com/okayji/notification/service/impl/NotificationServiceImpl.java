package com.okayji.notification.service.impl;

import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.mapper.NotificationMapper;
import com.okayji.notification.dto.NotificationResponse;
import com.okayji.notification.entity.Notification;
import com.okayji.notification.repository.NotificationRepository;
import com.okayji.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public void sendNotification(Notification notification) {
        notificationRepository.save(notification);

        messagingTemplate.convertAndSendToUser(
                notification.getUser().getId(),
                "/queue/notification",
                notificationMapper.toNotificationResponse(notification));
    }

    @Override
    public long unreadCount(String userId) {
        return notificationRepository.countByUserIdAndRead(userId, false);
    }

    @Override
    public void readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(AppError.NOTI_NOT_FOUND));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void readAll(String userId) {
        List<Notification> notificationList = notificationRepository.findByUserIdAndRead(userId, false);
        notificationList.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notificationList);
    }

    @Override
    public Page<NotificationResponse> findByUserId(String userId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        return notificationRepository.findByUserId(userId, pageable)
                .map(notificationMapper::toNotificationResponse);
    }
}
