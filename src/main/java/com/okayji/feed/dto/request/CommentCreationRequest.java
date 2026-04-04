package com.okayji.feed.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentCreationRequest {
    @NotBlank(message = "Post id is required")
    String postId;

    @NotBlank(message = "Content is required")
    String content;
}
