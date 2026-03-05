package com.okayji.mapper;

import com.okayji.moderation.dto.ModerationVerdict;
import com.okayji.moderation.entity.InputType;
import com.okayji.moderation.entity.ModerationResult;
import com.okayji.moderation.entity.TargetType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModerationMapper {
    ModerationResult toModerationResult(ModerationVerdict moderationVerdict,
                                        TargetType targetType,
                                        String targetId,
                                        InputType inputType);
}
