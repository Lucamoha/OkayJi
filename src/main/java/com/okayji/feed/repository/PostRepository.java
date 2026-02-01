package com.okayji.feed.repository;

import com.okayji.feed.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,String> {
    List<Post> findByUserId(String userId);
}
