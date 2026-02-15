package com.okayji.mapper;

import com.okayji.feed.dto.PostMediaDto;
import com.okayji.feed.dto.request.PostCreationRequest;
import com.okayji.feed.dto.request.PostUpdateRequest;
import com.okayji.feed.dto.response.PostResponse;
import com.okayji.feed.entity.Post;
import com.okayji.feed.entity.PostMedia;
import com.okayji.identity.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(source = "post.user.id", target = "userId")
    @Mapping(source = "post.user.profile.fullName", target = "userFullName")
    @Mapping(source = "post.user.profile.avatarUrl", target = "userAvatarUrl")
    @Mapping(source = "post.user.username", target = "username")
    @Mapping(source = "post.postMedia", target = "media")
    PostResponse toPostResponse(Post post, boolean liked, long likesCount,  long commentsCount);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(source = "user.id", target = "user.id")
    Post toPost(PostCreationRequest postCreationRequest, User user);

    void updatePost(@MappingTarget Post post, PostUpdateRequest postUpdateRequest);

    PostMediaDto toPostMediaDto(PostMedia postMedia);
}
