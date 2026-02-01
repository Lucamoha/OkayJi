package com.okayji.feed.repository;

import com.okayji.feed.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByPostId(String postId);
    List<Comment> findByUserIdAndPostId(String userId, String postId);
    long countByPost_Id(String postId);
}
