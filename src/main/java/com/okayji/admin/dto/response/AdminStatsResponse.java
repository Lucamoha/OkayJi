package com.okayji.admin.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminStatsResponse {
    long totalUsers;
    long activeUsers;
    long newUsersLast30Days;
    List<DailyCount> userGrowth;

    long totalPosts;
    Map<String, Long> postsByStatus;
}
