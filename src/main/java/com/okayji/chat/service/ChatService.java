package com.okayji.chat.service;

import com.okayji.chat.dto.request.CreateGroupChatRequest;
import com.okayji.chat.dto.response.ChatMemberResponse;
import com.okayji.chat.dto.response.ChatResponse;
import com.okayji.chat.dto.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChatService {
    void createDirectChat(String withUserId);
    Long unreadCount(String userId);
    ChatResponse createGroupChat(String userId, CreateGroupChatRequest createGroupChatRequest);
    Page<ChatResponse> getChats(String userId, int page, int size);
    ChatResponse getChat(String userId, String chatId);
    List<ChatMemberResponse> getMembers(String userId, String chatId);
    Page<MessageResponse> getMessages(String userId, String chatId, int page, int size);
}
