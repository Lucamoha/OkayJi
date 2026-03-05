package com.okayji.moderation.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OpenAiModerationRequest(String model, List<InputItem> input) {

    public static OpenAiModerationRequest from(String model, String imageUrl) {
        var items = new ArrayList<InputItem>();

        items.add(InputItem.imageUrl(imageUrl));

        return new OpenAiModerationRequest(model, items);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record InputItem(String type, ImageUrl image_url) {
        public static InputItem imageUrl(String url) {
            return new InputItem("image_url", new ImageUrl(url));
        }
    }

    public record ImageUrl(String url) {}
}
