package com.okayji.mapper;

import com.okayji.notification.dto.NotificationResponse;
import com.okayji.notification.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(source = "notification.user.id", target = "userId")
    NotificationResponse toNotificationResponse(Notification notification);
}
