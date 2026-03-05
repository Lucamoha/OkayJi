package com.okayji.moderation.listener;

import com.okayji.exception.AppError;
import com.okayji.exception.AppException;
import com.okayji.feed.entity.Comment;
import com.okayji.feed.entity.PostStatus;
import com.okayji.feed.repository.CommentRepository;
import com.okayji.mapper.ModerationMapper;
import com.okayji.moderation.dto.ModerationVerdict;
import com.okayji.moderation.entity.InputType;
import com.okayji.moderation.entity.ModerationDecision;
import com.okayji.moderation.entity.ModerationResult;
import com.okayji.moderation.entity.TargetType;
import com.okayji.moderation.event.CommentModerationEvent;
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
@Slf4j(topic = "COMMENT-MODERATION-LISTENER")
public class CommentModerationListener {

    private final CommentRepository commentRepository;
    private final ModerationResultRepository moderationResultRepository;
    private final ModerationService moderationService;
    private final ModerationMapper moderationMapper;

    @EventListener
    @Async
    public void handle(CommentModerationEvent event) {
        log.info("Received Comment Moderation Event with comment id={}", event.getSource());
        String commentId = (String) event.getSource();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(AppError.COMMENT_NOT_FOUND));

        if (comment.getContent() != null && !comment.getContent().isBlank()) {
            log.info("Moderating text comment id={}", event.getSource());
            ModerationVerdict verdict = moderationService.moderateText(comment.getContent());
            ModerationResult result = moderationMapper.toModerationResult(
                    verdict, TargetType.COMMENT, commentId, InputType.TEXT
            );
            moderationResultRepository.save(result);
        }

        if (isViolation(commentId)) // Delete if violated
            commentRepository.delete(comment);
    }

    private boolean isViolation(String commentId) {
        List<ModerationResult> moderationResults = moderationResultRepository.findByTargetId(commentId);

        for (ModerationResult m :  moderationResults) {
            if (m.getDecision().equals(ModerationDecision.BLOCK))
                return true;
        }
        return false;
    }
}
