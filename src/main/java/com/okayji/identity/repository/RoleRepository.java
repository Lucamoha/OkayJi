package com.okayji.identity.repository;

import com.okayji.identity.entity.Role;
import com.okayji.identity.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UserRole> {
}