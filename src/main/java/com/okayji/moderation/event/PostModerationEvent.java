package com.okayji.moderation.event;

import org.springframework.context.ApplicationEvent;

public class PostModerationEvent extends ApplicationEvent {

    public PostModerationEvent(String postId) {
        super(postId);
    }
}
