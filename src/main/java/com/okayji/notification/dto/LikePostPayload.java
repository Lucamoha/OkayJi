package com.okayji.notification.dto;

import lombok.Getter;

@Getter
public class LikePostPayload extends NotificationPayload {
    String postId;

    public LikePostPayload(String reviewTitle, String postId) {
        super(reviewTitle);
        this.postId = postId;
    }
}
