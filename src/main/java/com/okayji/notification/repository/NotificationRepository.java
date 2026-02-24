package com.okayji.notification.repository;

import com.okayji.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByUserIdAndRead(String userId, boolean read);
    Page<Notification> findByUserId(String userId, Pageable pageable);
    long countByUserIdAndRead(String userId, boolean read);
}
