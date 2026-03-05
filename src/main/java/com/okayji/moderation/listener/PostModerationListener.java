package com.okayji.moderation.listener;

import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.feed.entity.Post;
import com.okayji.feed.entity.PostMedia;
import com.okayji.feed.entity.PostMediaType;
import com.okayji.feed.entity.PostStatus;
import com.okayji.feed.repository.PostRepository;
import com.okayji.mapper.ModerationMapper;
import com.okayji.moderation.dto.ModerationVerdict;
import com.okayji.moderation.entity.InputType;
import com.okayji.moderation.entity.ModerationDecision;
import com.okayji.moderation.entity.ModerationResult;
import com.okayji.moderation.entity.TargetType;
import com.okayji.moderation.event.PostModerationEvent;
import com.okayji.moderation.repository.ModerationResultRepository;
import com.okayji.moderation.service.ModerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "POST-MODERATION-LISTENER")
public class PostModerationListener {

    private final PostRepository postRepository;
    private final ModerationResultRepository moderationResultRepository;
    private final ModerationService moderationService;
    private final ModerationMapper moderationMapper;

    @EventListener
    @Async
    public void handle(PostModerationEvent event) {
        log.info("Received Post Moderation Event with post id={}", event.getSource());
        String postId = (String) event.getSource();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppError.POST_NOT_FOUND));

        // TEXT
        if (post.getContent() != null && !post.getContent().isBlank()) {
            log.info("Moderating text post id={}", event.getSource());
            ModerationVerdict verdict = moderationService.moderateText(post.getContent());
            ModerationResult result = moderationMapper.toModerationResult(
                    verdict, TargetType.POST, post.getId(), InputType.TEXT
            );
            moderationResultRepository.save(result);
        }

        // IMAGE
        for (PostMedia m : post.getPostMedia()) {
            log.info("Moderating image post id={}", event.getSource());
            if (m.getType() != PostMediaType.IMAGE)
                continue;

            ModerationVerdict verdict = moderationService.moderateImageUrl(m.getMediaUrl());
            ModerationResult result = moderationMapper.toModerationResult(
                    verdict, TargetType.POST, post.getId(), InputType.IMAGE
            );
            moderationResultRepository.save(result);
        }

        PostStatus newStatus = decideFromDb(post.getId());
        post.setStatus(newStatus);
        postRepository.save(post);
    }

    private PostStatus decideFromDb(String postId) {
        List<ModerationResult> moderationResults = moderationResultRepository.findByTargetId(postId);
        boolean review = false;

        for (ModerationResult m :  moderationResults) {
            if (m.getDecision().equals(ModerationDecision.BLOCK))
                return PostStatus.REJECTED;
            if (m.getDecision().equals(ModerationDecision.REVIEW))
                review = true;
        }
        return review ? PostStatus.UNDER_REVIEW : PostStatus.PUBLISHED;
    }
}
