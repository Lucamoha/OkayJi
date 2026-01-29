package com.okayji.identity.repository;

import com.okayji.identity.entity.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPostRepository extends JpaRepository<UserPost,String> {
    List<UserPost> findByUserId(String userId);
}
