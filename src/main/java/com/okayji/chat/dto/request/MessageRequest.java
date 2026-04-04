package com.okayji.chat.dto.request;

import com.okayji.chat.entity.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class MessageRequest {
    @NotNull(message = "Message type is required")
    MessageType type;

    @NotBlank(message = "Content is required")
    String content;
}
