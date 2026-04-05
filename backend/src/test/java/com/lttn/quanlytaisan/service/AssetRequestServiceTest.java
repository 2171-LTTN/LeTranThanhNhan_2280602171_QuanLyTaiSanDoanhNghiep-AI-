package com.lttn.quanlytaisan.service;

import com.lttn.quanlytaisan.dto.request.AssignAssetRequest;
import com.lttn.quanlytaisan.dto.request.CreateAssetRequestDto;
import com.lttn.quanlytaisan.dto.request.ReviewAssetRequestDto;
import com.lttn.quanlytaisan.dto.response.AssetRequestResponse;
import com.lttn.quanlytaisan.exception.BusinessException;
import com.lttn.quanlytaisan.exception.ResourceNotFoundException;
import com.lttn.quanlytaisan.model.Asset;
import com.lttn.quanlytaisan.model.AssetRequest;
import com.lttn.quanlytaisan.model.AssetRequestCategory;
import com.lttn.quanlytaisan.model.AssetStatus;
import com.lttn.quanlytaisan.model.RequestStatus;
import com.lttn.quanlytaisan.model.User;
import com.lttn.quanlytaisan.repository.AssetRepository;
import com.lttn.quanlytaisan.repository.AssetRequestRepository;
import com.lttn.quanlytaisan.repository.UserRepository;
import com.lttn.quanlytaisan.security.SecurityHelper;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AssetRequestService Tests")
class AssetRequestServiceTest {

    @Mock
    private AssetRequestRepository assetRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetService assetService;

    @Mock
    private SecurityHelper securityHelper;

    @InjectMocks
    private AssetRequestService assetRequestService;

    private User testUser;
    private User adminUser;
    private AssetRequest testRequest;
    private Asset availableAsset;
    private AssetResponse mockAssetResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id("user-123")
                .name("Test User")
                .email("test@example.com")
                .role("USER")
                .isActive(true)
                .build();

        adminUser = User.builder()
                .id("admin-001")
                .name("Admin User")
                .email("admin@example.com")
                .role("ADMIN")
                .isActive(true)
                .build();

        testRequest = AssetRequest.builder()
                .id("req-001")
                .requestedByUserId("user-123")
                .requestedByUserName("Test User")
                .requestedByUserEmail("test@example.com")
                .category("LAPTOP")
                .reason("Need a laptop for work")
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        availableAsset = Asset.builder()
                .id("asset-001")
                .name("MacBook Pro 14-inch")
                .category("Laptop")
                .status(AssetStatus.AVAILABLE)
                .build();
    }

    @Nested
    @DisplayName("Create Request Tests")
    class CreateRequestTests {

        @Test
        @DisplayName("Should create asset request successfully")
        void shouldCreateAssetRequestSuccessfully() {
            CreateAssetRequestDto dto = CreateAssetRequestDto.builder()
                    .category(AssetRequestCategory.LAPTOP)
                    .reason("Need a laptop for work")
                    .note("For remote work")
                    .build();

            when(securityHelper.getCurrentUserIdOrThrow()).thenReturn("user-123");
            when(userRepository.findById("user-123")).thenReturn(Optional.of(testUser));
            when(assetRequestRepository.save(any(AssetRequest.class))).thenReturn(testRequest);

            AssetRequestResponse response = assetRequestService.createRequest(dto);

            assertThat(response).isNotNull();
            assertThat(response.getRequestedByUserId()).isEqualTo("user-123");
            assertThat(response.getStatus()).isEqualTo(RequestStatus.PENDING);

            verify(assetRequestRepository).save(any(AssetRequest.class));
        }
    }

    @Nested
    @DisplayName("Get Request Tests")
    class GetRequestTests {

        @Test
        @DisplayName("Should get my requests successfully")
        void shouldGetMyRequestsSuccessfully() {
            when(securityHelper.getCurrentUserIdOrThrow()).thenReturn("user-123");
            when(assetRequestRepository.findByRequestedByUserIdOrderByCreatedAtDesc("user-123"))
                    .thenReturn(List.of(testRequest));

            var responses = assetRequestService.getMyRequests();

            assertThat(responses).hasSize(1);
            assertThat(responses.get(0).getRequestedByUserId()).isEqualTo("user-123");
        }

        @Test
        @DisplayName("Should get all requests with pagination")
        void shouldGetAllRequestsWithPagination() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<AssetRequest> page = new PageImpl<>(List.of(testRequest));

            when(assetRequestRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(page);

            Page<AssetRequestResponse> responses = assetRequestService.getAllRequests(pageable);

            assertThat(responses.getTotalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should get pending requests with pagination")
        void shouldGetPendingRequestsWithPagination() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<AssetRequest> page = new PageImpl<>(List.of(testRequest));

            when(assetRequestRepository.findByStatusOrderByCreatedAtDesc(RequestStatus.PENDING, pageable))
                    .thenReturn(page);

            Page<AssetRequestResponse> responses = assetRequestService.getPendingRequests(pageable);

            assertThat(responses.getTotalElements()).isEqualTo(1);
            assertThat(responses.getContent().get(0).getStatus()).isEqualTo(RequestStatus.PENDING);
        }

        @Test
        @DisplayName("Should get request by ID successfully")
        void shouldGetRequestByIdSuccessfully() {
            when(assetRequestRepository.findById("req-001")).thenReturn(Optional.of(testRequest));

            AssetRequestResponse response = assetRequestService.getRequestById("req-001");

            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo("req-001");
        }

        @Test
        @DisplayName("Should throw exception when request not found")
        void shouldThrowExceptionWhenRequestNotFound() {
            when(assetRequestRepository.findById("nonexistent")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> assetRequestService.getRequestById("nonexistent"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Asset request not found");
        }
    }

    @Nested
    @DisplayName("Approve Request Tests")
    class ApproveRequestTests {

        @Test
        @DisplayName("Should approve request successfully")
        void shouldApproveRequestSuccessfully() {
            ReviewAssetRequestDto dto = ReviewAssetRequestDto.builder()
                    .assetId("asset-001")
                    .reviewNote("Approved for remote work")
                    .build();

            AssetRequest approvedRequest = AssetRequest.builder()
                    .id("req-001")
                    .requestedByUserId("user-123")
                    .requestedByUserName("Test User")
                    .requestedByUserEmail("test@example.com")
                    .category("LAPTOP")
                    .reason("Need a laptop for work")
                    .status(RequestStatus.APPROVED)
                    .reviewedBy("admin-001")
                    .reviewedByName("Admin User")
                    .reviewNote("Approved for remote work")
                    .assetId("asset-001")
                    .assetName("MacBook Pro 14-inch")
                    .reviewedAt(LocalDateTime.now())
                    .build();

            when(securityHelper.getCurrentUserIdOrThrow()).thenReturn("admin-001");
            when(userRepository.findById("admin-001")).thenReturn(Optional.of(adminUser));
            when(assetRequestRepository.findById("req-001")).thenReturn(Optional.of(testRequest));
            when(assetRepository.findById("asset-001")).thenReturn(Optional.of(availableAsset));
            when(assetService.assignAsset(eq("asset-001"), any(AssignAssetRequest.class), anyString()))
                    .thenReturn(mockAssetResponse);
            when(assetRequestRepository.save(any(AssetRequest.class))).thenReturn(approvedRequest);

            AssetRequestResponse response = assetRequestService.approveRequest("req-001", dto);

            assertThat(response).isNotNull();
            assertThat(response.getStatus()).isEqualTo(RequestStatus.APPROVED);
            assertThat(response.getReviewedByName()).isEqualTo("Admin User");

            verify(assetService).assignAsset(eq("asset-001"), any(AssignAssetRequest.class), anyString());
        }

        @Test
        @DisplayName("Should throw exception when approving non-pending request")
        void shouldThrowExceptionWhenApprovingNonPendingRequest() {
            testRequest.setStatus(RequestStatus.APPROVED);
            ReviewAssetRequestDto dto = ReviewAssetRequestDto.builder()
                    .assetId("asset-001")
                    .build();

            when(securityHelper.getCurrentUserIdOrThrow()).thenReturn("admin-001");
            when(userRepository.findById("admin-001")).thenReturn(Optional.of(adminUser));
            when(assetRequestRepository.findById("req-001")).thenReturn(Optional.of(testRequest));

            assertThatThrownBy(() -> assetRequestService.approveRequest("req-001", dto))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Only pending requests can be approved");
        }

        @Test
        @DisplayName("Should throw exception when asset is not available")
        void shouldThrowExceptionWhenAssetIsNotAvailable() {
            availableAsset.setStatus(AssetStatus.IN_USE);
            ReviewAssetRequestDto dto = ReviewAssetRequestDto.builder()
                    .assetId("asset-001")
                    .build();

            when(securityHelper.getCurrentUserIdOrThrow()).thenReturn("admin-001");
            when(userRepository.findById("admin-001")).thenReturn(Optional.of(adminUser));
            when(assetRequestRepository.findById("req-001")).thenReturn(Optional.of(testRequest));
            when(assetRepository.findById("asset-001")).thenReturn(Optional.of(availableAsset));

            assertThatThrownBy(() -> assetRequestService.approveRequest("req-001", dto))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("no longer available");
        }

        @Test
        @DisplayName("Should throw exception when asset category does not match")
        void shouldThrowExceptionWhenAssetCategoryDoesNotMatch() {
            availableAsset.setCategory("Monitor");
            ReviewAssetRequestDto dto = ReviewAssetRequestDto.builder()
                    .assetId("asset-001")
                    .build();

            when(securityHelper.getCurrentUserIdOrThrow()).thenReturn("admin-001");
            when(userRepository.findById("admin-001")).thenReturn(Optional.of(adminUser));
            when(assetRequestRepository.findById("req-001")).thenReturn(Optional.of(testRequest));
            when(assetRepository.findById("asset-001")).thenReturn(Optional.of(availableAsset));

            assertThatThrownBy(() -> assetRequestService.approveRequest("req-001", dto))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("does not match");
        }
    }

    @Nested
    @DisplayName("Reject Request Tests")
    class RejectRequestTests {

        @Test
        @DisplayName("Should reject request successfully")
        void shouldRejectRequestSuccessfully() {
            ReviewAssetRequestDto dto = ReviewAssetRequestDto.builder()
                    .reviewNote("Budget constraints")
                    .build();

            AssetRequest rejectedRequest = AssetRequest.builder()
                    .id("req-001")
                    .requestedByUserId("user-123")
                    .requestedByUserName("Test User")
                    .requestedByUserEmail("test@example.com")
                    .category("LAPTOP")
                    .reason("Need a laptop for work")
                    .status(RequestStatus.REJECTED)
                    .reviewedBy("admin-001")
                    .reviewedByName("Admin User")
                    .reviewNote("Budget constraints")
                    .reviewedAt(LocalDateTime.now())
                    .build();

            when(securityHelper.getCurrentUserIdOrThrow()).thenReturn("admin-001");
            when(userRepository.findById("admin-001")).thenReturn(Optional.of(adminUser));
            when(assetRequestRepository.findById("req-001")).thenReturn(Optional.of(testRequest));
            when(assetRequestRepository.save(any(AssetRequest.class))).thenReturn(rejectedRequest);

            AssetRequestResponse response = assetRequestService.rejectRequest("req-001", dto);

            assertThat(response).isNotNull();
            assertThat(response.getStatus()).isEqualTo(RequestStatus.REJECTED);
            assertThat(response.getReviewNote()).isEqualTo("Budget constraints");
        }

        @Test
        @DisplayName("Should throw exception when rejecting non-pending request")
        void shouldThrowExceptionWhenRejectingNonPendingRequest() {
            testRequest.setStatus(RequestStatus.APPROVED);
            ReviewAssetRequestDto dto = ReviewAssetRequestDto.builder()
                    .reviewNote("Rejected")
                    .build();

            when(securityHelper.getCurrentUserIdOrThrow()).thenReturn("admin-001");
            when(userRepository.findById("admin-001")).thenReturn(Optional.of(adminUser));
            when(assetRequestRepository.findById("req-001")).thenReturn(Optional.of(testRequest));

            assertThatThrownBy(() -> assetRequestService.rejectRequest("req-001", dto))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Only pending requests can be rejected");
        }
    }

    @Nested
    @DisplayName("Cancel Request Tests")
    class CancelRequestTests {

        @Test
        @DisplayName("Should cancel own request successfully")
        void shouldCancelOwnRequestSuccessfully() {
            AssetRequest cancelledRequest = AssetRequest.builder()
                    .id("req-001")
                    .requestedByUserId("user-123")
                    .requestedByUserName("Test User")
                    .requestedByUserEmail("test@example.com")
                    .category("LAPTOP")
                    .reason("Need a laptop for work")
                    .status(RequestStatus.CANCELLED)
                    .build();

            when(securityHelper.getCurrentUserIdOrThrow()).thenReturn("user-123");
            when(assetRequestRepository.findById("req-001")).thenReturn(Optional.of(testRequest));
            when(assetRequestRepository.save(any(AssetRequest.class))).thenReturn(cancelledRequest);

            AssetRequestResponse response = assetRequestService.cancelRequest("req-001");

            assertThat(response).isNotNull();
            assertThat(response.getStatus()).isEqualTo(RequestStatus.CANCELLED);
        }

        @Test
        @DisplayName("Should throw exception when cancelling other user's request")
        void shouldThrowExceptionWhenCancellingOtherUserRequest() {
            when(securityHelper.getCurrentUserIdOrThrow()).thenReturn("user-999");
            when(assetRequestRepository.findById("req-001")).thenReturn(Optional.of(testRequest));

            assertThatThrownBy(() -> assetRequestService.cancelRequest("req-001"))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("only cancel your own requests");
        }

        @Test
        @DisplayName("Should throw exception when cancelling non-pending request")
        void shouldThrowExceptionWhenCancellingNonPendingRequest() {
            testRequest.setStatus(RequestStatus.APPROVED);

            when(securityHelper.getCurrentUserIdOrThrow()).thenReturn("user-123");
            when(assetRequestRepository.findById("req-001")).thenReturn(Optional.of(testRequest));

            assertThatThrownBy(() -> assetRequestService.cancelRequest("req-001"))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Only pending requests can be cancelled");
        }
    }

    @Nested
    @DisplayName("Count Request Tests")
    class CountRequestTests {

        @Test
        @DisplayName("Should count pending requests")
        void shouldCountPendingRequests() {
            when(assetRequestRepository.countByStatus(RequestStatus.PENDING)).thenReturn(5L);

            long count = assetRequestService.countPendingRequests();

            assertThat(count).isEqualTo(5L);
        }

        @Test
        @DisplayName("Should count my pending requests")
        void shouldCountMyPendingRequests() {
            when(securityHelper.getCurrentUserIdOrThrow()).thenReturn("user-123");
            when(assetRequestRepository.countByRequestedByUserIdAndStatus("user-123", RequestStatus.PENDING))
                    .thenReturn(2L);

            long count = assetRequestService.countMyPendingRequests();

            assertThat(count).isEqualTo(2L);
        }
    }
}
