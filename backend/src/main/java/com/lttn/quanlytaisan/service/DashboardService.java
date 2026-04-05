package com.lttn.quanlytaisan.service;

import com.lttn.quanlytaisan.model.AssetStatus;
import com.lttn.quanlytaisan.repository.AssetRepository;
import com.lttn.quanlytaisan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        stats.put("availableAssets", assetRepository.countByStatus(AssetStatus.AVAILABLE));
        stats.put("inUseAssets", assetRepository.countByStatus(AssetStatus.IN_USE));
        stats.put("brokenAssets", assetRepository.countByStatus(AssetStatus.BROKEN));
        stats.put("totalUsers", userRepository.count());
        stats.put("adminUsers", userRepository.countByRole("ADMIN"));
        stats.put("staffUsers", userRepository.countByRole("USER"));

        return stats;
    }
}
