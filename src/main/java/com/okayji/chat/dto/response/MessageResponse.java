package com.okayji.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.okayji.chat.entity.MessageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {
    String chatId;
    Long id;
    Long seq;
    MessageType type;
    String content;
    String senderFullName;
    String senderAvatarUrl;
    String senderId;
    Instant createdAt;
}
