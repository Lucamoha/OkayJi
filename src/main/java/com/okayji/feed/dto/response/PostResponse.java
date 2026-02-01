package com.okayji.feed.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public class PostResponse {
    String id;
    String userId;
    String content;
    Instant createdAt;
    boolean liked;
    int likesCount;
    int commentsCount;
}
