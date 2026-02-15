package com.okayji.feed.dto.request;

import com.okayji.feed.dto.PostMediaDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCreationRequest {
    @NotBlank(message = "Content must not be blank")
    String content;
    List<PostMediaDto> media;
}
