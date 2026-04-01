package com.okayji.moderation.repository;

import com.okayji.moderation.entity.ModerationJob;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModerationJobRepository extends JpaRepository<ModerationJob,Long> {
    @Query("""
        select j
        from ModerationJob j
        where j.status = com.okayji.moderation.entity.ModerationJobStatus.PENDING
        and j.retryCount < j.maxRetries
        order by j.createdAt asc
    """)
    List<ModerationJob> findJobsForProcessing(Pageable pageable);

    @Modifying
    @Query("""
        update ModerationJob j
        set j.status = com.okayji.moderation.entity.ModerationJobStatus.PENDING
        where j.status = com.okayji.moderation.entity.ModerationJobStatus.PROCESSING
    """)
    int requeueProcessingJobs();
}
