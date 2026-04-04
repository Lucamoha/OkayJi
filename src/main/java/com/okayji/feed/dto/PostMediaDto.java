package com.okayji.feed.dto;

import com.okayji.feed.entity.PostMediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostMediaDto {
    @NotNull(message = "Media type is required")
    PostMediaType type;

    @NotBlank(message = "Media url is required")
    String mediaUrl;
}
