package com.okayji.moderation.dto;

import com.okayji.moderation.entity.ModerationDecision;

import java.util.List;
import java.util.Map;

public record ModerationVerdict(ModerationDecision decision,
                                boolean flagged,
                                Map<String, Boolean> categories,
                                Map<String, Double> categoryScores,
                                Map<String, List<String>> categoryAppliedInputTypes,
                                String provider, // "openai"
                                String model) {
}
