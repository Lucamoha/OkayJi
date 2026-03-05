package com.okayji.notification.controller;

import com.okayji.common.ApiResponse;
import com.okayji.identity.entity.User;
import com.okayji.notification.dto.NotificationResponse;
import com.okayji.notification.service.NotificationService;
import com.okayji.utils.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
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
                                                            @RequestParam(defaultValue = "20") int size,
                                                            @CurrentUser User currentUser) {
        return ApiResponse.<Page<NotificationResponse>>builder()
                .success(true)
                .data(notificationService.findByUserId(currentUser.getId(), page, size))
                .build();
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Get number of unread noti")
    ApiResponse<Long> getUnreadCount(@CurrentUser User currentUser) {
        return ApiResponse.<Long>builder()
                .success(true)
                .data(notificationService.unreadCount(currentUser.getId()))
                .build();
    }

    @PostMapping("/{notificationId}")
    @Operation(summary = "Read noti by id")
    @PreAuthorize("@permissionCheck.canReadNotification(#currentUser.id, #notificationId)")
    ApiResponse<?> readNotification(@PathVariable("notificationId") Long notificationId,
                                    @CurrentUser User currentUser) {
        notificationService.readNotification(notificationId);
        return ApiResponse.builder()
                .success(true)
                .build();
    }

    @PostMapping("/read-all")
    @Operation(summary = "Read all noti")
    ApiResponse<?> readAll(@CurrentUser User currentUser) {
        notificationService.readAll(currentUser.getId());
        return ApiResponse.builder()
                .success(true)
                .build();
    }
}
