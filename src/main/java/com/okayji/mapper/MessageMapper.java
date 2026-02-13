package com.okayji.mapper;

import com.okayji.chat.dto.response.MessageResponse;
import com.okayji.chat.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(source = "message.sender.profile.avatarUrl", target = "senderAvatarUrl")
    @Mapping(source = "message.sender.profile.fullName", target = "senderFullName")
    @Mapping(source = "message.sender.id", target = "senderId")
    @Mapping(source = "message.chat.id", target = "chatId")
    MessageResponse toMessageResponse(Message message);
}
