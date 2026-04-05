package com.lttn.quanlytaisan.service;

import com.lttn.quanlytaisan.model.AssetStatus;
import com.lttn.quanlytaisan.repository.AssetRepository;
import com.lttn.quanlytaisan.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DashboardService Tests")
class DashboardServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    @DisplayName("Should return all dashboard statistics")
    void shouldReturnAllDashboardStatistics() {
        when(assetRepository.count()).thenReturn(50L);
        when(assetRepository.countByStatus(AssetStatus.AVAILABLE)).thenReturn(20L);
        when(assetRepository.countByStatus(AssetStatus.IN_USE)).thenReturn(25L);
        when(assetRepository.countByStatus(AssetStatus.BROKEN)).thenReturn(5L);
        when(userRepository.count()).thenReturn(15L);
        when(userRepository.countByRole("ADMIN")).thenReturn(3L);
        when(userRepository.countByRole("USER")).thenReturn(12L);

        Map<String, Object> stats = dashboardService.getDashboardStats();

        assertThat(stats).isNotNull();
        assertThat(stats).hasSize(7);
        assertThat(stats.get("totalAssets")).isEqualTo(50L);
        assertThat(stats.get("availableAssets")).isEqualTo(20L);
        assertThat(stats.get("inUseAssets")).isEqualTo(25L);
        assertThat(stats.get("brokenAssets")).isEqualTo(5L);
        assertThat(stats.get("totalUsers")).isEqualTo(15L);
        assertThat(stats.get("adminUsers")).isEqualTo(3L);
        assertThat(stats.get("staffUsers")).isEqualTo(12L);
    }

    @Test
    @DisplayName("Should return zero when no data exists")
    void shouldReturnZeroWhenNoDataExists() {
        when(assetRepository.count()).thenReturn(0L);
        when(assetRepository.countByStatus(AssetStatus.AVAILABLE)).thenReturn(0L);
        when(assetRepository.countByStatus(AssetStatus.IN_USE)).thenReturn(0L);
        when(assetRepository.countByStatus(AssetStatus.BROKEN)).thenReturn(0L);
        when(userRepository.count()).thenReturn(0L);
        when(userRepository.countByRole("ADMIN")).thenReturn(0L);
        when(userRepository.countByRole("USER")).thenReturn(0L);

        Map<String, Object> stats = dashboardService.getDashboardStats();

        assertThat(stats).isNotNull();
        assertThat(stats.get("totalAssets")).isEqualTo(0L);
        assertThat(stats.get("availableAssets")).isEqualTo(0L);
        assertThat(stats.get("totalUsers")).isEqualTo(0L);
    }

    @Test
    @DisplayName("Should return correct stats when all assets are in use")
    void shouldReturnCorrectStatsWhenAllAssetsInUse() {
        when(assetRepository.count()).thenReturn(10L);
        when(assetRepository.countByStatus(AssetStatus.AVAILABLE)).thenReturn(0L);
        when(assetRepository.countByStatus(AssetStatus.IN_USE)).thenReturn(10L);
        when(assetRepository.countByStatus(AssetStatus.BROKEN)).thenReturn(0L);
        when(userRepository.count()).thenReturn(5L);
        when(userRepository.countByRole("ADMIN")).thenReturn(1L);
        when(userRepository.countByRole("USER")).thenReturn(4L);

        Map<String, Object> stats = dashboardService.getDashboardStats();

        assertThat(stats.get("inUseAssets")).isEqualTo(10L);
        assertThat(stats.get("availableAssets")).isEqualTo(0L);
        assertThat(stats.get("brokenAssets")).isEqualTo(0L);
    }
}
