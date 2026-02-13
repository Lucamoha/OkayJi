package com.okayji.mapper;

import com.okayji.chat.dto.response.ChatMemberResponse;
import com.okayji.chat.dto.response.ChatResponse;
import com.okayji.chat.entity.Chat;
import com.okayji.chat.entity.ChatMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    @Mapping(source = "chat.createdBy.id", target = "createdBy")
    ChatResponse toChatResponse(Chat chat, long unreadCount);

    @Mapping(source = "chatAvatarUrl", target = "chatAvatarUrl")
    @Mapping(source = "chatName", target = "chatName")
    @Mapping(source = "unreadCount", target = "unreadCount")
    @Mapping(source = "chat.createdBy.id", target = "createdBy")
    ChatResponse toChatResponse(Chat chat, long unreadCount, String chatAvatarUrl, String chatName);

    @Mapping(source = "chatMember.member.id", target = "memberId")
    @Mapping(source = "chatMember.chat.id", target = "chatId")
    @Mapping(source = "chatMember.member.profile.fullName", target = "memberFullName")
    @Mapping(source = "chatMember.member.profile.avatarUrl", target = "memberAvatarUrl")
    ChatMemberResponse toChatMemberResponse(ChatMember chatMember);
}
