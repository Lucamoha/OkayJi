package com.okayji.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class CreateGroupChatRequest {
    @NotBlank(message = "Chat name must not be blank")
    String chatName;
    String chatAvatarUrl;
    List<String> memberIds;
}
