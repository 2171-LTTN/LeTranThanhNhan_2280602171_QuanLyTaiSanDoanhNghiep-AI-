package com.lttn.quanlytaisan.service;

import com.lttn.quanlytaisan.dto.request.AssignAssetRequest;
import com.lttn.quanlytaisan.dto.request.CreateAssetRequest;
import com.lttn.quanlytaisan.dto.request.UpdateAssetRequest;
import com.lttn.quanlytaisan.dto.response.AssetResponse;
import com.lttn.quanlytaisan.exception.ResourceNotFoundException;
import com.lttn.quanlytaisan.model.Asset;
import com.lttn.quanlytaisan.model.AssetStatus;
import com.lttn.quanlytaisan.model.User;
import com.lttn.quanlytaisan.repository.AssetRepository;
import com.lttn.quanlytaisan.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AssetHistoryService assetHistoryService;

    @InjectMocks
    private AssetService assetService;

    private Asset testAsset;
    private User testUser;
    private CreateAssetRequest createRequest;

    @BeforeEach
    void setUp() {
        testAsset = Asset.builder()
                .id("asset-123")
                .name("MacBook Pro")
                .category("Electronics")
                .status(AssetStatus.AVAILABLE)
                .purchaseDate(LocalDate.of(2024, 1, 15))
                .build();

        testUser = User.builder()
                .id("user-123")
                .name("John Doe")
                .email("john@example.com")
                .role("USER")
                .build();

        createRequest = CreateAssetRequest.builder()
                .name("MacBook Pro")
                .category("Electronics")
                .purchaseDate("2024-01-15")
                .build();
    }

    @Test
    void createAsset_Success() {
        when(assetRepository.save(any(Asset.class))).thenReturn(testAsset);

        AssetResponse response = assetService.createAsset(createRequest, "admin@example.com");

        assertNotNull(response);
        assertEquals("asset-123", response.getId());
        assertEquals("MacBook Pro", response.getName());
        assertEquals("Electronics", response.getCategory());
        assertEquals(AssetStatus.AVAILABLE, response.getStatus());

        verify(assetRepository).save(any(Asset.class));
        verify(assetHistoryService).saveHistory(
                eq("asset-123"),
                eq("MacBook Pro"),
                isNull(),
                isNull(),
                eq("admin@example.com"),
                any(),
                anyString()
        );
    }

    @Test
    void getAllAssets_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Asset> page = new PageImpl<>(List.of(testAsset));
        when(assetRepository.findAll(pageable)).thenReturn(page);

        Page<AssetResponse> response = assetService.getAllAssets(pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals("MacBook Pro", response.getContent().get(0).getName());
    }

    @Test
    void getAssetById_Success() {
        when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));

        AssetResponse response = assetService.getAssetById("asset-123");

        assertNotNull(response);
        assertEquals("asset-123", response.getId());
        assertEquals("MacBook Pro", response.getName());
    }

    @Test
    void getAssetById_NotFound_ThrowsException() {
        when(assetRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> assetService.getAssetById("nonexistent"));
    }

    @Test
    void updateAsset_Success() {
        UpdateAssetRequest updateRequest = UpdateAssetRequest.builder()
                .name("MacBook Air")
                .category("Laptops")
                .build();

        when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));
        when(assetRepository.save(any(Asset.class))).thenReturn(testAsset);

        AssetResponse response = assetService.updateAsset("asset-123", updateRequest, "admin@example.com");

        assertNotNull(response);
        verify(assetRepository).save(any(Asset.class));
    }

    @Test
    void deleteAsset_Success() {
        when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));
        doNothing().when(assetRepository).delete(testAsset);

        assertDoesNotThrow(() -> assetService.deleteAsset("asset-123", "admin@example.com"));

        verify(assetRepository).delete(testAsset);
        verify(assetHistoryService).saveHistory(
                eq("asset-123"),
                eq("MacBook Pro"),
                isNull(),
                isNull(),
                eq("admin@example.com"),
                any(),
                anyString()
        );
    }

    @Test
    void assignAsset_Success() {
        AssignAssetRequest assignRequest = AssignAssetRequest.builder()
                .userId("user-123")
                .build();

        when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));
        when(userRepository.findById("user-123")).thenReturn(Optional.of(testUser));
        when(assetRepository.save(any(Asset.class))).thenAnswer(invocation -> {
            Asset saved = invocation.getArgument(0);
            saved.setId("asset-123");
            return saved;
        });

        AssetResponse response = assetService.assignAsset("asset-123", assignRequest, "admin@example.com");

        assertNotNull(response);
        assertEquals(AssetStatus.IN_USE, response.getStatus());
        verify(assetHistoryService).saveHistory(
                any(), any(), eq("user-123"), eq("John Doe"), eq("admin@example.com"), any(), anyString()
        );
    }

    @Test
    void assignAsset_AlreadyInUse_ThrowsException() {
        testAsset.setStatus(AssetStatus.IN_USE);
        AssignAssetRequest assignRequest = AssignAssetRequest.builder()
                .userId("user-123")
                .build();

        when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));

        assertThrows(IllegalStateException.class,
                () -> assetService.assignAsset("asset-123", assignRequest, "admin@example.com"));
    }

    @Test
    void assignAsset_UserNotFound_ThrowsException() {
        AssignAssetRequest assignRequest = AssignAssetRequest.builder()
                .userId("nonexistent-user")
                .build();

        when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));
        when(userRepository.findById("nonexistent-user")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> assetService.assignAsset("asset-123", assignRequest, "admin@example.com"));
    }

    @Test
    void returnAsset_Success() {
        testAsset.setStatus(AssetStatus.IN_USE);
        testAsset.setAssignedTo("user-123");

        when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));
        when(userRepository.findById("user-123")).thenReturn(Optional.of(testUser));
        when(assetRepository.save(any(Asset.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AssetResponse response = assetService.returnAsset("asset-123", "admin@example.com");

        assertNotNull(response);
        assertEquals(AssetStatus.AVAILABLE, response.getStatus());
    }

    @Test
    void returnAsset_NotAssigned_ThrowsException() {
        when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));

        assertThrows(IllegalStateException.class,
                () -> assetService.returnAsset("asset-123", "admin@example.com"));
    }
}
