package com.okayji.moderation.service;

import com.okayji.moderation.dto.ModerationVerdict;

import java.util.List;

public interface ModerationService {
    ModerationVerdict moderateText(String text);
    ModerationVerdict moderateImageUrl(String imageUrl);
    List<ModerationVerdict> moderateVideoUrl(String videoUrl);
}
