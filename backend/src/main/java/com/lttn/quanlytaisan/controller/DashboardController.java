package com.lttn.quanlytaisan.controller;

import com.lttn.quanlytaisan.dto.response.ApiResponse;
import com.lttn.quanlytaisan.service.AssetService;
import com.lttn.quanlytaisan.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final AssetService assetService;
    private final UserService userService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        log.debug("GET /api/dashboard/stats");

        Map<String, Long> assetStats = assetService.getAssetStats();
        Map<String, Long> userStats = userService.getUserStats();

        Map<String, Object> combined = new HashMap<>();
        combined.put("totalAssets", assetStats.getOrDefault("total", 0L));
        combined.put("availableAssets", assetStats.getOrDefault("available", 0L));
        combined.put("inUseAssets", assetStats.getOrDefault("inUse", 0L));
        combined.put("brokenAssets", assetStats.getOrDefault("broken", 0L));
        combined.put("totalUsers", userStats.getOrDefault("total", 0L));
        combined.put("adminUsers", userStats.getOrDefault("admins", 0L));
        combined.put("staffUsers", userStats.getOrDefault("users", 0L));

        return ResponseEntity.ok(ApiResponse.success(combined));
    }
}
