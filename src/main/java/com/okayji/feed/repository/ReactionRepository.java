package com.okayji.feed.repository;

import com.okayji.feed.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReactionRepository extends JpaRepository<Reaction,String> {
    boolean existsByPostIdAndUserId(String postId, String userId);
    long countByPostId(String postId);

    @Modifying
    @Query("""
    delete from Reaction r
    where r.user.id = :userId
    and r.post.id = :postId
    """)
    void deleteForUnReact(@Param("userId") String userId,
                          @Param("postId") String postId);
}
