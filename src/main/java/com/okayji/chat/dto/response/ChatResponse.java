package com.okayji.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.okayji.chat.entity.ChatType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatResponse {
    String id;
    ChatType type;
    String createdBy;
    String chatAvatarUrl;
    String chatName;
    Instant createdAt;
    Instant lastMessageAt;
    long unreadCount = 0L;
}
