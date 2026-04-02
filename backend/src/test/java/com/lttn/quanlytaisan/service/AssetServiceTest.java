package com.lttn.quanlytaisan.service;

import com.lttn.quanlytaisan.dto.request.AssignAssetRequest;
import com.lttn.quanlytaisan.dto.request.CreateAssetRequest;
import com.lttn.quanlytaisan.dto.request.UpdateAssetRequest;
import com.lttn.quanlytaisan.dto.response.AssetResponse;
import com.lttn.quanlytaisan.exception.BusinessException;
import com.lttn.quanlytaisan.exception.ResourceNotFoundException;
import com.lttn.quanlytaisan.mapper.AssetMapper;
import com.lttn.quanlytaisan.model.Asset;
import com.lttn.quanlytaisan.model.AssetStatus;
import com.lttn.quanlytaisan.model.User;
import com.lttn.quanlytaisan.repository.AssetRepository;
import com.lttn.quanlytaisan.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AssetService Tests")
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AssetHistoryService assetHistoryService;

    @Mock
    private AssetMapper assetMapper;

    @InjectMocks
    private AssetService assetService;

    private Asset testAsset;
    private User testUser;
    private AssetResponse assetResponse;

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

        assetResponse = AssetResponse.builder()
                .id("asset-123")
                .name("MacBook Pro")
                .category("Electronics")
                .status(AssetStatus.AVAILABLE)
                .build();
    }

    @Nested
    @DisplayName("Create Asset Tests")
    class CreateAssetTests {

        @Test
        @DisplayName("Should create asset successfully")
        void shouldCreateAssetSuccessfully() {
            CreateAssetRequest request = CreateAssetRequest.builder()
                    .name("MacBook Pro")
                    .category("Electronics")
                    .purchaseDate("2024-01-15")
                    .build();

            when(assetRepository.save(any(Asset.class))).thenReturn(testAsset);
            when(assetMapper.toResponse(testAsset)).thenReturn(assetResponse);

            AssetResponse response = assetService.createAsset(request, "admin@example.com");

            assertThat(response).isNotNull();
            assertThat(response.getName()).isEqualTo("MacBook Pro");
            assertThat(response.getStatus()).isEqualTo(AssetStatus.AVAILABLE);

            verify(assetRepository).save(any(Asset.class));
            verify(assetHistoryService).saveHistory(any(), any(), any(), any(), any(), any(), any());
        }
    }

    @Nested
    @DisplayName("Get Asset Tests")
    class GetAssetTests {

        @Test
        @DisplayName("Should get all assets")
        void shouldGetAllAssets() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Asset> page = new PageImpl<>(List.of(testAsset));

            when(assetRepository.findAll(pageable)).thenReturn(page);
            when(assetMapper.toResponse(testAsset)).thenReturn(assetResponse);

            Page<AssetResponse> response = assetService.getAllAssets(pageable);

            assertThat(response).isNotNull();
            assertThat(response.getTotalElements()).isEqualTo(1);
            assertThat(response.getContent().get(0).getName()).isEqualTo("MacBook Pro");
        }

        @Test
        @DisplayName("Should get asset by ID")
        void shouldGetAssetById() {
            when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));
            when(assetMapper.toResponse(testAsset)).thenReturn(assetResponse);

            AssetResponse response = assetService.getAssetById("asset-123");

            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo("asset-123");
        }

        @Test
        @DisplayName("Should throw exception when asset not found")
        void shouldThrowExceptionWhenAssetNotFound() {
            when(assetRepository.findById("nonexistent")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> assetService.getAssetById("nonexistent"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Asset not found");
        }
    }

    @Nested
    @DisplayName("Update Asset Tests")
    class UpdateAssetTests {

        @Test
        @DisplayName("Should update asset successfully")
        void shouldUpdateAssetSuccessfully() {
            UpdateAssetRequest updateRequest = UpdateAssetRequest.builder()
                    .name("MacBook Air")
                    .category("Laptops")
                    .build();

            when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));
            when(assetRepository.save(any(Asset.class))).thenReturn(testAsset);
            when(assetMapper.toResponse(testAsset)).thenReturn(assetResponse);

            AssetResponse response = assetService.updateAsset("asset-123", updateRequest, "admin@example.com");

            assertThat(response).isNotNull();
            verify(assetRepository).save(any(Asset.class));
            verify(assetHistoryService).saveHistory(any(), any(), any(), any(), any(), any(), any());
        }
    }

    @Nested
    @DisplayName("Delete Asset Tests")
    class DeleteAssetTests {

        @Test
        @DisplayName("Should delete asset successfully")
        void shouldDeleteAssetSuccessfully() {
            when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));
            doNothing().when(assetRepository).delete(testAsset);

            assetService.deleteAsset("asset-123", "admin@example.com");

            verify(assetRepository).delete(testAsset);
            verify(assetHistoryService).saveHistory(any(), any(), any(), any(), any(), any(), any());
        }
    }

    @Nested
    @DisplayName("Assign Asset Tests")
    class AssignAssetTests {

        @Test
        @DisplayName("Should assign available asset successfully")
        void shouldAssignAvailableAssetSuccessfully() {
            AssignAssetRequest assignRequest = AssignAssetRequest.builder()
                    .userId("user-123")
                    .build();

            when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));
            when(userRepository.findById("user-123")).thenReturn(Optional.of(testUser));
            when(assetRepository.save(any(Asset.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(assetMapper.toResponse(any(Asset.class))).thenReturn(assetResponse);

            AssetResponse response = assetService.assignAsset("asset-123", assignRequest, "admin@example.com");

            assertThat(response).isNotNull();
            verify(assetHistoryService).saveHistory(any(), any(), eq("user-123"), eq("John Doe"), any(), any(), any());
        }

        @Test
        @DisplayName("Should throw exception when asset is already in use")
        void shouldThrowExceptionWhenAssetAlreadyInUse() {
            testAsset.setStatus(AssetStatus.IN_USE);
            AssignAssetRequest assignRequest = AssignAssetRequest.builder()
                    .userId("user-123")
                    .build();

            when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));

            assertThatThrownBy(() -> assetService.assignAsset("asset-123", assignRequest, "admin@example.com"))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("already in use");
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            AssignAssetRequest assignRequest = AssignAssetRequest.builder()
                    .userId("nonexistent-user")
                    .build();

            when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));
            when(userRepository.findById("nonexistent-user")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> assetService.assignAsset("asset-123", assignRequest, "admin@example.com"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User not found");
        }
    }

    @Nested
    @DisplayName("Return Asset Tests")
    class ReturnAssetTests {

        @Test
        @DisplayName("Should return assigned asset successfully")
        void shouldReturnAssignedAssetSuccessfully() {
            testAsset.setStatus(AssetStatus.IN_USE);
            testAsset.setAssignedTo("user-123");

            when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));
            when(userRepository.findById("user-123")).thenReturn(Optional.of(testUser));
            when(assetRepository.save(any(Asset.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(assetMapper.toResponse(any(Asset.class))).thenReturn(assetResponse);

            AssetResponse response = assetService.returnAsset("asset-123", "admin@example.com");

            assertThat(response).isNotNull();
            verify(assetHistoryService).saveHistory(any(), any(), eq("user-123"), eq("John Doe"), any(), any(), any());
        }

        @Test
        @DisplayName("Should throw exception when asset is not assigned")
        void shouldThrowExceptionWhenAssetNotAssigned() {
            when(assetRepository.findById("asset-123")).thenReturn(Optional.of(testAsset));

            assertThatThrownBy(() -> assetService.returnAsset("asset-123", "admin@example.com"))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("not currently assigned");
        }
    }
}
