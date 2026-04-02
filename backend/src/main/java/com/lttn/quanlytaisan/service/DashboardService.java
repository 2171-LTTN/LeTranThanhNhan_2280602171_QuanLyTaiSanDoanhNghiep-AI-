package com.lttn.quanlytaisan.service;

import com.lttn.quanlytaisan.model.AssetStatus;
import com.lttn.quanlytaisan.repository.AssetRepository;
import com.lttn.quanlytaisan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final AssetRepository assetRepository;
    private final UserRepository userRepository;

    public Map<String, Object> getDashboardStats() {
        log.debug("Fetching dashboard statistics");

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalAssets", assetRepository.count());
        stats.put("availableAssets", assetRepository.findByStatus(AssetStatus.AVAILABLE, Pageable.unpaged()).getTotalElements());
        stats.put("inUseAssets", assetRepository.findByStatus(AssetStatus.IN_USE, Pageable.unpaged()).getTotalElements());
        stats.put("brokenAssets", assetRepository.findByStatus(AssetStatus.BROKEN, Pageable.unpaged()).getTotalElements());
        stats.put("totalUsers", userRepository.count());
        stats.put("adminUsers", userRepository.countByRole("ADMIN"));
        stats.put("staffUsers", userRepository.countByRole("USER"));

        return stats;
    }
}
