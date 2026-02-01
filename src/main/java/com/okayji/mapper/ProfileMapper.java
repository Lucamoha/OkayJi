package com.okayji.mapper;

import com.okayji.identity.dto.request.ProfileUpdateRequest;
import com.okayji.identity.dto.response.ProfileResponse;
import com.okayji.identity.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileResponse toProfileResponse(Profile profile);
    void updateProfile(@MappingTarget Profile profile, ProfileUpdateRequest profileUpdateRequest);
}
