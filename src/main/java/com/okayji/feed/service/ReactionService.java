package com.okayji.feed.service;

public interface ReactionService {
    void like(String userId, String postId);
    void unlike(String userId, String postId);
}
