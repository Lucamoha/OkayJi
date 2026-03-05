package com.okayji.moderation.service;

import com.okayji.moderation.dto.ModerationVerdict;

public interface ModerationService {
    ModerationVerdict moderateText(String text);
    ModerationVerdict moderateImageUrl(String imageUr);
}
