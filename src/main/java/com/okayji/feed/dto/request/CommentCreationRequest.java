package com.okayji.feed.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentCreationRequest {
    String postId;
    @NotBlank(message = "Content must not be blank")
    String content;
}
