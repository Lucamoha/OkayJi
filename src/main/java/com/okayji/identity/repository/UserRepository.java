package com.okayji.identity.repository;

import com.okayji.identity.entity.User;
import com.okayji.identity.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    User findUserById(String id);
    User findByUsernameIgnoreCase(String username);
    Optional<User> findUserByIdOrUsername(String id, String username);

    long countByStatus(UserStatus status);

    long countByCreatedAtAfter(Instant since);

    @Query("""
        select cast(u.createdAt as date), count(u)
        from User u
        where u.createdAt >= :since
        group by cast(u.createdAt as date)
        order by cast(u.createdAt as date)
    """)
    List<Object[]> countNewUsersGroupedByDay(@Param("since") Instant since);
}
