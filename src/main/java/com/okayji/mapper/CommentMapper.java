package com.okayji.mapper;

import com.okayji.feed.dto.request.CommentCreationRequest;
import com.okayji.feed.dto.response.CommentResponse;
import com.okayji.feed.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentCreationRequest commentCreationRequest);

    @Mapping(source = "comment.user.id", target = "userId")
    @Mapping(source = "comment.post.id", target = "postId")
    CommentResponse toCommentResponse(Comment comment);
}
