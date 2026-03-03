package com.okayji.feed.dto;

import com.okayji.feed.entity.PostMediaType;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostMediaDto {
    @NotBlank(message = "Media type must not be blank")
    PostMediaType type;
    @NotBlank(message = "Media url must not be blank")
    String mediaUrl;
}
