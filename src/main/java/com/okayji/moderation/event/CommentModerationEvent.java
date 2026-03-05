package com.okayji.moderation.event;

import org.springframework.context.ApplicationEvent;

public class CommentModerationEvent extends ApplicationEvent {

    public CommentModerationEvent(String commentId) {
        super(commentId);
    }
}
