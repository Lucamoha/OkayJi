package com.okayji.feed.repository;

import com.okayji.feed.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
    long countByPost_Id(String postId);
    Page<Comment> findByPost_Id(String postId, Pageable pageable);
}
