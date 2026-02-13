package com.okayji.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.okayji.enums.ChatEvent;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatUpdateEvent {
    @Enumerated(EnumType.STRING)
    ChatEvent chatEvent;
    String chatId;
    Long messageSeq;
}
