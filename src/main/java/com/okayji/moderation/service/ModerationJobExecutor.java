package com.okayji.moderation.service;

import com.okayji.feed.repository.PostRepository;
import com.okayji.moderation.entity.ModerationJob;
import com.okayji.moderation.entity.ModerationJobStatus;
import com.okayji.moderation.repository.ModerationJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j(topic = "MODERATION-JOB-EXECUTOR")
public class ModerationJobExecutor {
    private final ModerationJobRepository moderationJobRepository;
    private final PostRepository postRepository;
    private final ModerationOrchestrator moderationOrchestrator;
    private final ModerationJobExecutor self;

    public ModerationJobExecutor(ModerationJobRepository moderationJobRepository,
                                 PostRepository postRepository,
                                 ModerationOrchestrator moderationOrchestrator,
                                 @Lazy ModerationJobExecutor moderationJobExecutor) {
        this.moderationJobRepository = moderationJobRepository;
        this.postRepository = postRepository;
        this.moderationOrchestrator = moderationOrchestrator;
        this.self = moderationJobExecutor;
    }

    public void prepareAndExecute(Long jobId) {
        self.markProcessing(jobId);
        self.execute(jobId);
    }

    @Retryable(
            retryFor = {
                    org.springframework.web.client.ResourceAccessException.class,
                    java.net.SocketTimeoutException.class,
                    java.io.IOException.class,
                    java.net.ConnectException.class
            },
            maxAttempts = 2,
            backoff = @Backoff(delay = 2000)
    )
    public void execute(Long jobId) {
        Optional<ModerationJob> optJob = moderationJobRepository.findById(jobId);
        if (optJob.isEmpty())
            return;

        ModerationJob job = optJob.get();
        switch (job.getTargetType()) {
            case POST -> moderationOrchestrator.processPost(job);
            case COMMENT -> moderationOrchestrator.processComment(job);
        }

        self.markDone(jobId);
    }

    @Recover
    public void recover(Throwable ex, Long jobId) {
        ModerationJob job = moderationJobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Moderation job not found: " + jobId));

        log.error("Moderation job {} failed after retries", jobId, ex);

        if (job.getRetryCount() < job.getMaxRetries()) {
            job.setStatus(ModerationJobStatus.PENDING);
        } else {
            job.setStatus(ModerationJobStatus.FAILED);
        }

        moderationJobRepository.save(job);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markProcessing(Long jobId) {
        ModerationJob job = moderationJobRepository.findById(jobId).orElseThrow();

        if (job.getStatus() == ModerationJobStatus.DONE
                || job.getStatus() == ModerationJobStatus.FAILED) {
            return;
        }

        job.setStatus(ModerationJobStatus.PROCESSING);
        job.setRetryCount(job.getRetryCount() + 1);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markDone(Long jobId) {
        ModerationJob job = moderationJobRepository.findById(jobId).orElseThrow();
        job.setStatus(ModerationJobStatus.DONE);
    }
}
