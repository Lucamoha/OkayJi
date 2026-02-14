package com.okayji.chat.controller;

import com.okayji.chat.dto.request.CreateGroupChatRequest;
import com.okayji.chat.dto.response.ChatMemberResponse;
import com.okayji.chat.dto.response.ChatResponse;
import com.okayji.chat.dto.response.MessageResponse;
import com.okayji.chat.service.ChatService;
import com.okayji.common.ApiResponse;
import com.okayji.identity.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@AllArgsConstructor
@Tag(name = "Chat Controller")
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    @Operation(summary = "Get user chats list")
    ApiResponse<Page<ChatResponse>> getMyChats(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.<Page<ChatResponse>>builder()
                .success(true)
                .data(chatService.getChats(getCurrentUser().getId(), page, size))
                .build();
    }

    @GetMapping("/{chatId}")
    @Operation(summary = "Get chat")
    ApiResponse<ChatResponse> getChat(@PathVariable String chatId) {
        return ApiResponse.<ChatResponse>builder()
                .success(true)
                .data(chatService.getChat(getCurrentUser().getId(), chatId))
                .build();
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Get number of unread chats")
    ApiResponse<Long> getUnreadCount() {
        return ApiResponse.<Long>builder()
                .success(true)
                .data(chatService.unreadCount(getCurrentUser().getId()))
                .build();
    }

    @GetMapping("/{chatId}/members")
    @Operation(summary = "Get members in chat")
    ApiResponse<List<ChatMemberResponse>> getMembers(@PathVariable String chatId) {
        return ApiResponse.<List<ChatMemberResponse>>builder()
                .success(true)
                .data(chatService.getMembers(getCurrentUser().getId(), chatId))
                .build();
    }

    @GetMapping("/{chatId}/messages")
    @Operation(summary = "Get messages in chat")
    ApiResponse<Page<MessageResponse>> getMessages(@PathVariable String chatId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.<Page<MessageResponse>>builder()
                .success(true)
                .data(chatService.getMessages(getCurrentUser().getId(), chatId, page, size))
                .build();
    }

    @PostMapping("/group")
    @Operation(summary = "Create group chat with others")
    ApiResponse<ChatResponse> createGroupChat(@Valid @RequestBody CreateGroupChatRequest request) {
        return ApiResponse.<ChatResponse>builder()
                .success(true)
                .data(chatService.createGroupChat(getCurrentUser().getId(), request))
                .build();
    }

    @PostMapping("/group/{groupId}/leave")
    @Operation(summary = "Leave group chat")
    ApiResponse<?> leaveGroupChat(@PathVariable String groupId) {
        chatService.leaveGroupChat(getCurrentUser().getId(), groupId);
        return ApiResponse.builder()
                .success(true)
                .build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
