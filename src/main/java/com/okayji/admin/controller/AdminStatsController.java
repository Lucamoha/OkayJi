package com.okayji.admin.controller;

import com.okayji.admin.dto.response.AdminStatsResponse;
import com.okayji.admin.service.AdminStatsService;
import com.okayji.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/stats")
@AllArgsConstructor
@Tag(name = "Admin Stats Controller")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    @GetMapping
    @Operation(summary = "Get platform stats", description = "Returns aggregated stats: users, user growth (last 30 days), posts by status")
    ApiResponse<AdminStatsResponse> getStats() {
        return ApiResponse.<AdminStatsResponse>builder()
                .success(true)
                .data(adminStatsService.getStats())
                .build();
    }
}
