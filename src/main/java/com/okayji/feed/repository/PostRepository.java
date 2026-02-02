package com.okayji.feed.repository;

import com.okayji.feed.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,String> {
    Page<Post> findByUser_Id(String userId, Pageable pageable);
}
