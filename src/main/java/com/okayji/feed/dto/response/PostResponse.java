package com.okayji.feed.dto.response;

import com.okayji.enums.PostStatus;
import com.okayji.feed.dto.PostMediaDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public class PostResponse {
    String id;
    String userId;
    String username;
    String userFullName;
    String userAvatarUrl;
    String content;
    Instant createdAt;
    PostStatus status;
    boolean liked;
    long likesCount;
    long commentsCount;
    List<PostMediaDto> media;
}
