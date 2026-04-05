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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetRequestService {

    private final AssetRequestRepository assetRequestRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final AssetService assetService;
    private final SecurityHelper securityHelper;

    public AssetRequestResponse createRequest(CreateAssetRequestDto dto) {
        String userId = securityHelper.getCurrentUserIdOrThrow();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        AssetRequest request = AssetRequest.builder()
                .requestedByUserId(userId)
                .requestedByUserName(user.getName())
                .requestedByUserEmail(user.getEmail())
                .assetName(null)
                .category(dto.getCategory().name())
                .reason(dto.getReason())
                .note(dto.getNote())
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        AssetRequest saved = assetRequestRepository.save(request);
        log.info("Asset request created: {} by user: {}", saved.getId(), user.getEmail());

        return AssetRequestResponse.fromEntity(saved);
    }

    public List<AssetRequestResponse> getMyRequests() {
        String userId = securityHelper.getCurrentUserIdOrThrow();
        return assetRequestRepository.findByRequestedByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(AssetRequestResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<AssetRequestResponse> getAllRequests(Pageable pageable) {
        return assetRequestRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(AssetRequestResponse::fromEntity);
    }

    public Page<AssetRequestResponse> getPendingRequests(Pageable pageable) {
        return assetRequestRepository.findByStatusOrderByCreatedAtDesc(RequestStatus.PENDING, pageable)
                .map(AssetRequestResponse::fromEntity);
    }

    public AssetRequestResponse getRequestById(String id) {
        AssetRequest request = assetRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset request not found: " + id));
        return AssetRequestResponse.fromEntity(request);
    }

    public AssetRequestResponse approveRequest(String requestId, ReviewAssetRequestDto dto) {
        String reviewerId = securityHelper.getCurrentUserIdOrThrow();
        User currentUser = userRepository.findById(reviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + reviewerId));

        AssetRequest savedRequest = assetRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset request not found: " + requestId));

        if (savedRequest.getStatus() != RequestStatus.PENDING) {
            throw new BusinessException("Only pending requests can be approved");
        }

        if (dto.getAssetId() == null || dto.getAssetId().isBlank()) {
            throw new BusinessException("Please select an asset to allocate when approving");
        }

        Asset asset = assetRepository.findById(dto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + dto.getAssetId()));

        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            log.warn("Approve request {} failed — asset {} is not available (status: {})",
                    requestId, asset.getId(), asset.getStatus());
            throw new BusinessException(
                    "Selected asset is no longer available. Please choose another asset.");
        }

        AssetRequestCategory requestedCategory = AssetRequestCategory.fromString(savedRequest.getCategory());
        if (!requestedCategory.matchesAssetCategory(asset.getCategory())) {
            throw new BusinessException(
                    "Selected asset category does not match the requested category. Choose an asset in the same category.");
        }

        String performedBy = securityHelper.getCurrentUserEmailOrDefault("system");
        assetService.assignAsset(
                asset.getId(),
                new AssignAssetRequest(savedRequest.getRequestedByUserId()),
                performedBy
        );

        savedRequest.setStatus(RequestStatus.APPROVED);
        savedRequest.setReviewedBy(reviewerId);
        savedRequest.setReviewedByName(currentUser.getName());
        savedRequest.setReviewNote(dto.getReviewNote());
        savedRequest.setReviewedAt(LocalDateTime.now());
        savedRequest.setAssetId(asset.getId());
        savedRequest.setAssetName(asset.getName());

        try {
            AssetRequest updated = assetRequestRepository.save(savedRequest);
            log.info("Asset request {} approved by: {}, assigned asset: {}", requestId, currentUser.getEmail(), asset.getName());
            return AssetRequestResponse.fromEntity(updated);
        } catch (OptimisticLockingFailureException e) {
            log.warn("Concurrent modification detected for request {} — another user may have processed it", requestId);
            throw new BusinessException(
                    "This request was already processed by another user. Please refresh and try again.");
        }
    }

    public AssetRequestResponse rejectRequest(String requestId, ReviewAssetRequestDto dto) {
        String reviewerId = securityHelper.getCurrentUserIdOrThrow();
        User currentUser = userRepository.findById(reviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + reviewerId));

        AssetRequest savedRequest = assetRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset request not found: " + requestId));

        if (savedRequest.getStatus() != RequestStatus.PENDING) {
            throw new BusinessException("Only pending requests can be rejected");
        }

        savedRequest.setStatus(RequestStatus.REJECTED);
        savedRequest.setReviewedBy(reviewerId);
        savedRequest.setReviewedByName(currentUser.getName());
        savedRequest.setReviewNote(dto.getReviewNote());
        savedRequest.setReviewedAt(LocalDateTime.now());

        AssetRequest updated = assetRequestRepository.save(savedRequest);
        log.info("Asset request {} rejected by: {}", requestId, currentUser.getEmail());

        return AssetRequestResponse.fromEntity(updated);
    }

    public AssetRequestResponse cancelRequest(String requestId) {
        String userId = securityHelper.getCurrentUserIdOrThrow();

        AssetRequest request = assetRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset request not found: " + requestId));

        if (!request.getRequestedByUserId().equals(userId)) {
            throw new BusinessException("You can only cancel your own requests");
        }

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new BusinessException("Only pending requests can be cancelled");
        }

        request.setStatus(RequestStatus.CANCELLED);
        AssetRequest updated = assetRequestRepository.save(request);
        log.info("Asset request {} cancelled by: {}", requestId, userId);

        return AssetRequestResponse.fromEntity(updated);
    }

    public long countPendingRequests() {
        return assetRequestRepository.countByStatus(RequestStatus.PENDING);
    }

    public long countMyPendingRequests() {
        String userId = securityHelper.getCurrentUserIdOrThrow();
        return assetRequestRepository.countByRequestedByUserIdAndStatus(userId, RequestStatus.PENDING);
    }
}
