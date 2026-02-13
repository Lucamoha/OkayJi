package com.okayji.chat.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public class ChatMemberResponse {
    String memberFullName;
    String memberAvatarUrl;
    String memberId;
    String chatId;
}
