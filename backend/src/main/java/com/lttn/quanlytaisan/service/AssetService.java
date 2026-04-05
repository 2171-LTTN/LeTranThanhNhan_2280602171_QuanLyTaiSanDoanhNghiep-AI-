package com.lttn.quanlytaisan.service;

import com.lttn.quanlytaisan.dto.request.AssignAssetRequest;
import com.lttn.quanlytaisan.dto.request.CreateAssetRequest;
import com.lttn.quanlytaisan.dto.request.UpdateAssetRequest;
import com.lttn.quanlytaisan.dto.response.AssetResponse;
import com.lttn.quanlytaisan.exception.BusinessException;
import com.lttn.quanlytaisan.exception.ResourceNotFoundException;
import com.lttn.quanlytaisan.mapper.AssetMapper;
import com.lttn.quanlytaisan.model.Asset;
import com.lttn.quanlytaisan.model.AssetAction;
import com.lttn.quanlytaisan.model.AssetStatus;
import com.lttn.quanlytaisan.model.User;
import com.lttn.quanlytaisan.repository.AssetRepository;
import com.lttn.quanlytaisan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetService {

    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final AssetHistoryService assetHistoryService;
    private final AssetMapper assetMapper;

    public AssetResponse createAsset(CreateAssetRequest request, String performedBy) {
        log.info("Creating asset: '{}' by user: {}", request.getName(), performedBy);

        if (assetRepository.existsBySerialNumber(request.getSerialNumber())) {
            log.warn("Asset creation failed - serial number already exists: {}", request.getSerialNumber());
            throw new BusinessException("Serial number already exists: " + request.getSerialNumber());
        }

        Asset asset = Asset.builder()
                .name(request.getName().trim())
                .category(request.getCategory().trim())
                .status(AssetStatus.AVAILABLE)
                .purchaseDate(parseDateOrThrow(request.getPurchaseDate()))
                .purchasePrice(request.getPurchasePrice())
                .serialNumber(request.getSerialNumber())
                .brand(request.getBrand())
                .model(request.getModel())
                .warrantyUntil(parseDateOrThrow(request.getWarrantyUntil()))
                .location(request.getLocation())
                .note(request.getNote())
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
                buildCreationDetails(saved)
        );

        log.info("Asset created successfully: {} (ID: {})", saved.getName(), saved.getId());

        return assetMapper.toResponse(saved);
    }

    public Page<AssetResponse> getAllAssets(Pageable pageable) {
        log.debug("Fetching all assets, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return assetRepository.findAll(pageable).map(assetMapper::toResponse);
    }

    public Page<AssetResponse> getAssetsByStatus(AssetStatus status, Pageable pageable) {
        log.debug("Fetching assets by status: {}, page: {}", status, pageable.getPageNumber());
        return assetRepository.findByStatus(status, pageable).map(assetMapper::toResponse);
    }

    public Page<AssetResponse> getAssetsByUser(String userId, Pageable pageable) {
        log.debug("Fetching assets for user: {}, page: {}", userId, pageable.getPageNumber());
        return assetRepository.findByAssignedTo(userId, pageable).map(assetMapper::toResponse);
    }

    public AssetResponse getAssetById(String id) {
        log.debug("Fetching asset by ID: {}", id);
        Asset asset = findAssetByIdOrThrow(id);
        return assetMapper.toResponse(asset);
    }

    public AssetResponse updateAsset(String id, UpdateAssetRequest request, String performedBy) {
        log.info("Updating asset: {} by user: {}", id, performedBy);

        Asset asset = findAssetByIdOrThrow(id);
        String oldName = asset.getName();
        String oldCategory = asset.getCategory();

        // Check for duplicate serial number — only when serial is actually being changed
        if (request.getSerialNumber() != null
                && !request.getSerialNumber().isBlank()
                && !request.getSerialNumber().equals(asset.getSerialNumber())) {
            if (assetRepository.existsBySerialNumber(request.getSerialNumber())) {
                log.warn("Asset update failed - serial number already exists: {}", request.getSerialNumber());
                throw new BusinessException("Serial number already exists: " + request.getSerialNumber());
            }
        }

        applyUpdates(asset, request);
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

        log.info("Asset updated successfully: {} (ID: {})", saved.getName(), saved.getId());

        return assetMapper.toResponse(saved);
    }

    public void deleteAsset(String id, String performedBy) {
        log.info("Deleting asset: {} by user: {}", id, performedBy);

        Asset asset = findAssetByIdOrThrow(id);

        if (asset.getStatus() == AssetStatus.IN_USE) {
            log.warn("Cannot delete asset that is currently in use: {}", asset.getId());
            throw new BusinessException("Cannot delete asset that is currently assigned. Please return it first.");
        }

        assetHistoryService.saveHistory(
                asset.getId(),
                asset.getName(),
                null,
                null,
                performedBy,
                AssetAction.DELETED,
                buildDeletionDetails(asset)
        );

        assetRepository.delete(asset);

        log.info("Asset deleted successfully: {} (ID: {})", asset.getName(), asset.getId());
    }

    public AssetResponse assignAsset(String assetId, AssignAssetRequest request, String performedBy) {
        log.info("Assigning asset: {} to user: {} by: {}", assetId, request.getUserId(), performedBy);

        Asset asset = findAssetByIdOrThrow(assetId);
        validateAssetForAssignment(asset);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> {
                    log.warn("Assignment failed - user not found: {}", request.getUserId());
                    return new ResourceNotFoundException("User not found: " + request.getUserId());
                });

        if (user.getIsActive() == null || !user.getIsActive()) {
            log.warn("Assignment failed - user is not active: {}", request.getUserId());
            throw new BusinessException("Cannot assign to inactive user");
        }

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
                buildAssignmentDetails(user)
        );

        log.info("Asset assigned successfully: {} to {} (ID: {})", saved.getName(), user.getName(), saved.getId());

        return assetMapper.toResponse(saved);
    }

    public AssetResponse returnAsset(String assetId, String performedBy) {
        log.info("Returning asset: {} by user: {}", assetId, performedBy);

        Asset asset = findAssetByIdOrThrow(assetId);
        validateAssetForReturn(asset);

        String previousUserId = asset.getAssignedTo();
        User previousUser = userRepository.findById(previousUserId).orElse(null);
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
                buildReturnDetails(userName)
        );

        log.info("Asset returned successfully: {} (ID: {})", saved.getName(), saved.getId());

        return assetMapper.toResponse(saved);
    }

    // USER returns their own assigned asset
    public AssetResponse returnMyAsset(String assetId, String userId, String performedBy) {
        log.info("User {} returning their asset: {}", userId, assetId);

        Asset asset = findAssetByIdOrThrow(assetId);

        if (!userId.equals(asset.getAssignedTo())) {
            log.warn("User {} attempted to return asset {} which is not assigned to them", userId, assetId);
            throw new BusinessException("You can only return assets that are assigned to you");
        }

        validateAssetForReturn(asset);

        asset.setAssignedTo(null);
        asset.setStatus(AssetStatus.AVAILABLE);
        asset.setUpdatedAt(LocalDateTime.now());

        Asset saved = assetRepository.save(asset);

        assetHistoryService.saveHistory(
                saved.getId(),
                saved.getName(),
                userId,
                performedBy,
                performedBy,
                AssetAction.RETURNED,
                buildReturnDetails(performedBy)
        );

        log.info("Asset {} returned by user {}", saved.getName(), userId);

        return assetMapper.toResponse(saved);
    }

    /** USER: report an asset as broken (must be assigned to them). */
    public AssetResponse reportAssetBroken(String assetId, String userId, String performedBy) {
        log.info("User {} reporting asset {} as broken", userId, assetId);

        Asset asset = findAssetByIdOrThrow(assetId);

        if (!userId.equals(asset.getAssignedTo())) {
            log.warn("User {} cannot report asset {} as broken — not assigned to them", userId, assetId);
            throw new BusinessException("You can only report assets that are assigned to you");
        }

        asset.setStatus(AssetStatus.BROKEN);
        asset.setUpdatedAt(LocalDateTime.now());

        Asset saved = assetRepository.save(asset);

        assetHistoryService.saveHistory(
                saved.getId(),
                saved.getName(),
                userId,
                performedBy,
                performedBy,
                AssetAction.REPORTED_BROKEN,
                String.format("Asset reported as broken by %s", performedBy)
        );

        log.info("Asset {} reported as broken by user {}", saved.getName(), userId);
        return assetMapper.toResponse(saved);
    }

    /** ADMIN: mark a broken asset as repaired and available. */
    public AssetResponse markAssetAvailable(String assetId, String performedBy) {
        log.info("Admin {} marking asset {} as available after repair", performedBy, assetId);

        Asset asset = findAssetByIdOrThrow(assetId);

        if (asset.getStatus() != AssetStatus.BROKEN) {
            log.warn("Cannot mark asset {} as available — status is {}", assetId, asset.getStatus());
            throw new BusinessException("Only broken assets can be marked as available after repair");
        }

        asset.setAssignedTo(null);
        asset.setStatus(AssetStatus.AVAILABLE);
        asset.setUpdatedAt(LocalDateTime.now());

        Asset saved = assetRepository.save(asset);

        assetHistoryService.saveHistory(
                saved.getId(),
                saved.getName(),
                null,
                null,
                performedBy,
                AssetAction.REPAIRED,
                "Asset repaired and marked as available"
        );

        log.info("Asset {} marked as available by {}", saved.getName(), performedBy);
        return assetMapper.toResponse(saved);
    }

    public Map<String, Long> getAssetStats() {
        log.debug("Fetching asset statistics");

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", assetRepository.count());
        stats.put("available", assetRepository.countByStatus(AssetStatus.AVAILABLE));
        stats.put("inUse", assetRepository.countByStatus(AssetStatus.IN_USE));
        stats.put("broken", assetRepository.countByStatus(AssetStatus.BROKEN));

        log.debug("Asset statistics: {}", stats);

        return stats;
    }

    private Asset findAssetByIdOrThrow(String id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Asset not found: {}", id);
                    return new ResourceNotFoundException("Asset not found: " + id);
                });
    }

    private void validateAssetForAssignment(Asset asset) {
        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            log.warn("Asset cannot be assigned - current status: {}", asset.getStatus());
            throw new BusinessException("Asset is not available for assignment. Current status: " + asset.getStatus());
        }
    }

    private void validateAssetForReturn(Asset asset) {
        if (asset.getStatus() != AssetStatus.IN_USE || asset.getAssignedTo() == null) {
            log.warn("Asset is not currently assigned: {}", asset.getId());
            throw new BusinessException("Asset is not currently assigned");
        }
    }

    private void applyUpdates(Asset asset, UpdateAssetRequest request) {
        if (request.getName() != null && !request.getName().isBlank()) {
            asset.setName(request.getName().trim());
        }
        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            asset.setCategory(request.getCategory().trim());
        }
        if (request.getSerialNumber() != null && !request.getSerialNumber().isBlank()) {
            asset.setSerialNumber(request.getSerialNumber());
        }
        if (request.getBrand() != null) {
            asset.setBrand(request.getBrand());
        }
        if (request.getModel() != null) {
            asset.setModel(request.getModel());
        }
        if (request.getLocation() != null) {
            asset.setLocation(request.getLocation());
        }
        if (request.getNote() != null) {
            asset.setNote(request.getNote());
        }
        if (request.getPurchaseDate() != null) {
            asset.setPurchaseDate(parseDateOrThrow(request.getPurchaseDate()));
        }
        if (request.getPurchasePrice() != null) {
            asset.setPurchasePrice(request.getPurchasePrice());
        }
        if (request.getWarrantyUntil() != null) {
            asset.setWarrantyUntil(parseDateOrThrow(request.getWarrantyUntil()));
        }
    }

    private LocalDate parseDateOrThrow(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            log.warn("Invalid date format: {}", dateStr);
            throw new BusinessException("Invalid date format: " + dateStr + ". Expected format: yyyy-MM-dd");
        }
    }

    private String buildCreationDetails(Asset asset) {
        return String.format("Asset '%s' (Category: %s) created", asset.getName(), asset.getCategory());
    }

    private String buildDeletionDetails(Asset asset) {
        return String.format("Asset '%s' deleted", asset.getName());
    }

    private String buildAssignmentDetails(User user) {
        return String.format("Asset assigned to %s", user.getName());
    }

    private String buildReturnDetails(String userName) {
        return String.format("Asset returned by %s", userName);
    }

    private String buildUpdateDetails(String oldName, String oldCategory, UpdateAssetRequest request) {
        StringBuilder sb = new StringBuilder();
        if (request.getName() != null && !request.getName().isBlank() && !request.getName().equals(oldName)) {
            sb.append("Name: ").append(oldName).append(" -> ").append(request.getName()).append("; ");
        }
        if (request.getCategory() != null && !request.getCategory().isBlank() && !request.getCategory().equals(oldCategory)) {
            sb.append("Category: ").append(oldCategory).append(" -> ").append(request.getCategory());
        }
        return sb.length() > 0 ? sb.toString().trim() : "No changes made";
    }
}
