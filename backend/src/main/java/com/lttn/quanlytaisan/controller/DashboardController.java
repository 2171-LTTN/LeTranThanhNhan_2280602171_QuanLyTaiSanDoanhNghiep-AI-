package com.lttn.quanlytaisan.controller;

import com.lttn.quanlytaisan.dto.response.ApiResponse;
import com.lttn.quanlytaisan.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        log.debug("GET /api/dashboard/stats");

        Map<String, Object> stats = dashboardService.getDashboardStats();

        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
