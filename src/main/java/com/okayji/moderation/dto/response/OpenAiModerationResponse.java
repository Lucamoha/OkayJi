package com.okayji.moderation.dto.response;

import java.util.List;
import java.util.Map;

public record OpenAiModerationResponse(String id,
                                       String model,
                                       List<Result> results) {
    public record Result(
            boolean flagged,
            Map<String, Boolean> categories,
            Map<String, Double> category_scores,
            Map<String, List<String>> category_applied_input_types
    ) {}
}
