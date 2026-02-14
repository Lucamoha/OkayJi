package com.okayji.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class UpdateGroupChatRequest {
    @NotBlank(message = "Chat name must not be blank")
    String chatName;
    String chatAvatarUrl;
}
