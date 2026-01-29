package com.okayji.identity.repository;

import com.okayji.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
    User findByUsernameIgnoreCase(String username);
}
