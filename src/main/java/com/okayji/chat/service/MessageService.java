package com.okayji.chat.service;

import com.okayji.chat.dto.request.MessageRequest;

public interface MessageService {
    void sendMessage(String chatId, String userId, MessageRequest messageRequest);
    void markAsRead(String chatId, String userId, Long messageSeq);
}
