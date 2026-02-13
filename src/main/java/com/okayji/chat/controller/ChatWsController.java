package com.okayji.chat.controller;

import com.okayji.chat.dto.request.MessageRequest;
import com.okayji.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final MessageService messageService;

    @MessageMapping("/chats/{chatId}/messages.send")
    void sendMessage(@DestinationVariable String chatId,
                     @Payload MessageRequest messageRequest,
                     Principal principal) {
        messageService.sendMessage(chatId, principal.getName(), messageRequest);
    }

    @MessageMapping("/chats/{chatId}/messages.read/{messageSeq}")
    void markRead(@DestinationVariable String chatId,
                  @DestinationVariable long messageSeq,
                  Principal principal) {
        messageService.markAsRead(chatId, principal.getName(), messageSeq);
    }
}
