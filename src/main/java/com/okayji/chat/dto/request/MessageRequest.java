package com.okayji.chat.dto.request;

import com.okayji.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class MessageRequest {
    @NotBlank(message = "Message type must not be blank")
    MessageType type;
    @NotBlank(message = "Content must not be blank")
    String content;
}
