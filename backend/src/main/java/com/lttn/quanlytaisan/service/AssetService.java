package com.lttn.quanlytaisan.service;

import com.lttn.quanlytaisan.dto.request.AssignAssetRequest;
import com.lttn.quanlytaisan.dto.request.CreateAssetRequest;
import com.lttn.quanlytaisan.dto.request.UpdateAssetRequest;
import com.lttn.quanlytaisan.dto.response.AssetResponse;
import com.lttn.quanlytaisan.exception.ResourceNotFoundException;
import com.lttn.quanlytaisan.model.Asset;
import com.lttn.quanlytaisan.model.AssetAction;
import com.lttn.quanlytaisan.model.AssetStatus;
import com.lttn.quanlytaisan.model.User;
import com.lttn.quanlytaisan.repository.AssetRepository;
import com.lttn.quanlytaisan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final AssetHistoryService assetHistoryService;

    public AssetResponse createAsset(CreateAssetRequest request, String performedBy) {
        Asset asset = Asset.builder()
                .name(request.getName())
                .category(request.getCategory())
                .status(AssetStatus.AVAILABLE)
                .purchaseDate(parseDate(request.getPurchaseDate()))
                .createdAt(LocalDateTime.now())
                .build();

        Asset saved = assetRepository.save(asset);

        assetHistoryService.saveHistory(
                saved.getId(),
                saved.getName(),
                null,
                null,
                performedBy,
                AssetAction.CREATED,
                "Asset created: " + saved.getName()
        );

        return mapToResponse(saved);
    }

    public Page<AssetResponse> getAllAssets(Pageable pageable) {
        return assetRepository.findAll(pageable).map(this::mapToResponse);
    }

    public Page<AssetResponse> getAssetsByStatus(AssetStatus status, Pageable pageable) {
        return assetRepository.findByStatus(status, pageable).map(this::mapToResponse);
    }

    public Page<AssetResponse> getAssetsByUser(String userId, Pageable pageable) {
        return assetRepository.findByAssignedTo(userId, pageable).map(this::mapToResponse);
    }

    public AssetResponse getAssetById(String id) {
        Asset asset = findAssetById(id);
        return mapToResponse(asset);
    }

    public AssetResponse updateAsset(String id, UpdateAssetRequest request, String performedBy) {
        Asset asset = findAssetById(id);
        String oldName = asset.getName();
        String oldCategory = asset.getCategory();

        if (request.getName() != null && !request.getName().isBlank()) {
            asset.setName(request.getName());
        }
        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            asset.setCategory(request.getCategory());
        }
        asset.setUpdatedAt(LocalDateTime.now());

        Asset saved = assetRepository.save(asset);

        String details = buildUpdateDetails(oldName, oldCategory, request);

        assetHistoryService.saveHistory(
                saved.getId(),
                saved.getName(),
                null,
                null,
                performedBy,
                AssetAction.UPDATED,
                details
        );

        return mapToResponse(saved);
    }

    public void deleteAsset(String id, String performedBy) {
        Asset asset = findAssetById(id);

        assetHistoryService.saveHistory(
                asset.getId(),
                asset.getName(),
                null,
                null,
                performedBy,
                AssetAction.DELETED,
                "Asset deleted: " + asset.getName()
        );

        assetRepository.delete(asset);
    }

    public AssetResponse assignAsset(String assetId, AssignAssetRequest request, String performedBy) {
        Asset asset = findAssetById(assetId);

        if (asset.getStatus() == AssetStatus.IN_USE) {
            throw new IllegalStateException("Asset is already in use");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));

        asset.setAssignedTo(user.getId());
        asset.setStatus(AssetStatus.IN_USE);
        asset.setUpdatedAt(LocalDateTime.now());

        Asset saved = assetRepository.save(asset);

        assetHistoryService.saveHistory(
                saved.getId(),
                saved.getName(),
                user.getId(),
                user.getName(),
                performedBy,
                AssetAction.ASSIGNED,
                "Asset assigned to " + user.getName()
        );

        return mapToResponse(saved);
    }

    public AssetResponse returnAsset(String assetId, String performedBy) {
        Asset asset = findAssetById(assetId);

        if (asset.getStatus() != AssetStatus.IN_USE || asset.getAssignedTo() == null) {
            throw new IllegalStateException("Asset is not currently assigned");
        }

        String previousUserId = asset.getAssignedTo();
        User previousUser = null;
        if (previousUserId != null) {
            previousUser = userRepository.findById(previousUserId).orElse(null);
        }

        String userName = previousUser != null ? previousUser.getName() : "Unknown";

        asset.setAssignedTo(null);
        asset.setStatus(AssetStatus.AVAILABLE);
        asset.setUpdatedAt(LocalDateTime.now());

        Asset saved = assetRepository.save(asset);

        assetHistoryService.saveHistory(
                saved.getId(),
                saved.getName(),
                previousUserId,
                userName,
                performedBy,
                AssetAction.RETURNED,
                "Asset returned by " + userName
        );

        return mapToResponse(saved);
    }

    public Map<String, Long> getAssetStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", assetRepository.count());
        stats.put("available", assetRepository.findByStatus(AssetStatus.AVAILABLE,
            Pageable.unpaged()).getTotalElements());
        stats.put("inUse", assetRepository.findByStatus(AssetStatus.IN_USE,
            Pageable.unpaged()).getTotalElements());
        stats.put("broken", assetRepository.findByStatus(AssetStatus.BROKEN,
            Pageable.unpaged()).getTotalElements());
        return stats;
    }

    private Asset findAssetById(String id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + id));
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    private String buildUpdateDetails(String oldName, String oldCategory, UpdateAssetRequest request) {
        StringBuilder sb = new StringBuilder();
        if (request.getName() != null && !request.getName().equals(oldName)) {
            sb.append("Name: ").append(oldName).append(" -> ").append(request.getName()).append("; ");
        }
        if (request.getCategory() != null && !request.getCategory().equals(oldCategory)) {
            sb.append("Category: ").append(oldCategory).append(" -> ").append(request.getCategory());
        }
        return sb.length() > 0 ? sb.toString() : "No changes";
    }

    private AssetResponse mapToResponse(Asset asset) {
        String assignedToName = null;
        if (asset.getAssignedTo() != null) {
            assignedToName = userRepository.findById(asset.getAssignedTo())
                    .map(User::getName)
                    .orElse(null);
        }

        return AssetResponse.builder()
                .id(asset.getId())
                .name(asset.getName())
                .category(asset.getCategory())
                .status(asset.getStatus())
                .assignedTo(asset.getAssignedTo())
                .assignedToName(assignedToName)
                .purchaseDate(asset.getPurchaseDate())
                .createdAt(asset.getCreatedAt())
                .updatedAt(asset.getUpdatedAt())
                .build();
    }
}
