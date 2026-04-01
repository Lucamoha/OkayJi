package com.okayji.admin.service.impl;

import com.okayji.admin.dto.response.AdminStatsResponse;
import com.okayji.admin.dto.response.DailyCount;
import com.okayji.admin.service.AdminStatsService;
import com.okayji.feed.entity.PostStatus;
import com.okayji.feed.repository.PostRepository;
import com.okayji.identity.entity.UserStatus;
import com.okayji.identity.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j(topic = "ADMIN-STATS-SERVICE")
public class AdminStatsServiceImpl implements AdminStatsService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public AdminStatsResponse getStats() {
        // User stats
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByStatus(UserStatus.ACTIVE);

        Instant thirtyDaysAgo = Instant.now().minus(30, ChronoUnit.DAYS);
        long newUsersLast30Days = userRepository.countByCreatedAtAfter(thirtyDaysAgo);

        List<Object[]> rawGrowth = userRepository.countNewUsersGroupedByDay(thirtyDaysAgo);
        List<DailyCount> userGrowth = rawGrowth.stream()
                .map(row -> DailyCount.builder()
                        .date(((java.sql.Date) row[0]).toLocalDate())
                        .count(((Number) row[1]).longValue())
                        .build())
                .toList();

        // Post stats
        long totalPosts = postRepository.count();
        Map<String, Long> postsByStatus = new LinkedHashMap<>();
        Arrays.stream(PostStatus.values())
                .forEach(status -> postsByStatus.put(status.name(), postRepository.countByStatus(status)));

        return AdminStatsResponse.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .newUsersLast30Days(newUsersLast30Days)
                .userGrowth(userGrowth)
                .totalPosts(totalPosts)
                .postsByStatus(postsByStatus)
                .build();
    }
}
