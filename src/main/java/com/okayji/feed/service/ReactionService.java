package com.okayji.feed.service;

public interface ReactionService {
    void like(String postId);
    void unlike(String postId);
}
