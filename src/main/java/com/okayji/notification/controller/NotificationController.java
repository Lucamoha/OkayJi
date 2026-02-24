package com.okayji.notification.controller;

import com.okayji.common.ApiResponse;
import com.okayji.identity.entity.User;
import com.okayji.notification.dto.NotificationResponse;
import com.okayji.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@AllArgsConstructor
@Tag(name = "Notification Controller")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get user noti list")
    ApiResponse<Page<NotificationResponse>> getNotification(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.<Page<NotificationResponse>>builder()
                .success(true)
                .data(notificationService.findByUserId(getCurrentUser().getId(), page, size))
                .build();
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Get number of unread noti")
    ApiResponse<Long> getUnreadCount() {
        return ApiResponse.<Long>builder()
                .success(true)
                .data(notificationService.unreadCount(getCurrentUser().getId()))
                .build();
    }

    @PostMapping("/{notificationId}")
    @Operation(summary = "Read noti by id")
    ApiResponse<?> readNotification(@PathVariable("notificationId") Long notificationId) {
        notificationService.readNotification(notificationId);
        return ApiResponse.builder()
                .success(true)
                .build();
    }

    @PostMapping("/read-all")
    @Operation(summary = "Read all noti")
    ApiResponse<?> readAll() {
        notificationService.readAll(getCurrentUser().getId());
        return ApiResponse.builder()
                .success(true)
                .build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
