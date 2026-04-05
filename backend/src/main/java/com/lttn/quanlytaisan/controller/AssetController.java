package com.lttn.quanlytaisan.controller;

import com.lttn.quanlytaisan.dto.request.AssignAssetRequest;
import com.lttn.quanlytaisan.dto.request.CreateAssetRequest;
import com.lttn.quanlytaisan.dto.request.UpdateAssetRequest;
import com.lttn.quanlytaisan.dto.response.ApiResponse;
import com.lttn.quanlytaisan.dto.response.AssetHistoryResponse;
import com.lttn.quanlytaisan.dto.response.AssetResponse;
import com.lttn.quanlytaisan.model.AssetStatus;
import com.lttn.quanlytaisan.security.SecurityHelper;
import com.lttn.quanlytaisan.service.AssetHistoryService;
import com.lttn.quanlytaisan.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Slf4j
public class AssetController {

    private final AssetService assetService;
    private final AssetHistoryService assetHistoryService;
    private final SecurityHelper securityHelper;

    // ===== USER & ADMIN: View Assets =====

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AssetResponse>>> getAllAssets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) AssetStatus status,
            @RequestParam(required = false) String userId
    ) {
        log.debug("GET /api/assets - page: {}, size: {}, status: {}, userId: {}", page, size, status, userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<AssetResponse> assets;
        if (status != null) {
            assets = assetService.getAssetsByStatus(status, pageable);
        } else if (userId != null && !userId.isBlank()) {
            assets = assetService.getAssetsByUser(userId, pageable);
        } else {
            assets = assetService.getAllAssets(pageable);
        }

        return ResponseEntity.ok(ApiResponse.success(assets));
    }

    @GetMapping("/my-assets")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<AssetResponse>>> getMyAssets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String currentUserId = securityHelper.getCurrentUserIdOrThrow();
        log.debug("GET /api/assets/my-assets - userId: {}, page: {}", currentUserId, page);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AssetResponse> assets = assetService.getAssetsByUser(currentUserId, pageable);

        return ResponseEntity.ok(ApiResponse.success(assets));
    }

    @GetMapping("/histories")
    public ResponseEntity<ApiResponse<List<AssetHistoryResponse>>> getAllHistories() {
        log.debug("GET /api/assets/histories");

        List<AssetHistoryResponse> histories = assetHistoryService.getAllHistories();

        return ResponseEntity.ok(ApiResponse.success(histories));
    }

    @GetMapping("/my-histories")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<AssetHistoryResponse>>> getMyHistories() {
        String currentUserId = securityHelper.getCurrentUserIdOrThrow();
        log.debug("GET /api/assets/my-histories - userId: {}", currentUserId);

        List<AssetHistoryResponse> histories = assetHistoryService.getUserAssetHistory(currentUserId);

        return ResponseEntity.ok(ApiResponse.success(histories));
    }

    /** Static path must be registered before /{id} so "stats" is not captured as an asset id. */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getAssetStats() {
        log.debug("GET /api/assets/stats");

        Map<String, Long> stats = assetService.getAssetStats();

        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetResponse>> getAssetById(@PathVariable String id) {
        log.debug("GET /api/assets/{}", id);

        AssetResponse asset = assetService.getAssetById(id);

        return ResponseEntity.ok(ApiResponse.success(asset));
    }

    // ===== ADMIN ONLY: Asset Management =====

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AssetResponse>> createAsset(
            @Valid @RequestBody CreateAssetRequest request
    ) {
        log.info("POST /api/assets - Creating asset: {}", request.getName());

        String performedBy = securityHelper.getCurrentUserEmailOrDefault("system");
        AssetResponse asset = assetService.createAsset(request, performedBy);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(asset, "Asset created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AssetResponse>> updateAsset(
            @PathVariable String id,
            @Valid @RequestBody UpdateAssetRequest request
    ) {
        log.info("PUT /api/assets/{}", id);

        String performedBy = securityHelper.getCurrentUserEmailOrDefault("system");
        AssetResponse asset = assetService.updateAsset(id, request, performedBy);

        return ResponseEntity.ok(ApiResponse.success(asset, "Asset updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAsset(@PathVariable String id) {
        log.info("DELETE /api/assets/{}", id);

        String performedBy = securityHelper.getCurrentUserEmailOrDefault("system");
        assetService.deleteAsset(id, performedBy);

        return ResponseEntity.ok(ApiResponse.success(null, "Asset deleted successfully"));
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AssetResponse>> assignAsset(
            @PathVariable String id,
            @Valid @RequestBody AssignAssetRequest request
    ) {
        log.info("POST /api/assets/{}/assign - to user: {}", id, request.getUserId());

        String performedBy = securityHelper.getCurrentUserEmailOrDefault("system");
        AssetResponse asset = assetService.assignAsset(id, request, performedBy);

        return ResponseEntity.ok(ApiResponse.success(asset, "Asset assigned successfully"));
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AssetResponse>> returnAsset(@PathVariable String id) {
        log.info("POST /api/assets/{}/return", id);

        String performedBy = securityHelper.getCurrentUserEmailOrDefault("system");
        AssetResponse asset = assetService.returnAsset(id, performedBy);

        return ResponseEntity.ok(ApiResponse.success(asset, "Asset returned successfully"));
    }

    // USER can return their own assigned asset
    @PostMapping("/{id}/return-mine")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<AssetResponse>> returnMyAsset(@PathVariable String id) {
        String currentUserId = securityHelper.getCurrentUserIdOrThrow();
        String currentUserEmail = securityHelper.getCurrentUserEmailOrDefault("system");
        log.info("POST /api/assets/{}/return-mine - userId: {}", id, currentUserId);

        AssetResponse asset = assetService.returnMyAsset(id, currentUserId, currentUserEmail);

        return ResponseEntity.ok(ApiResponse.success(asset, "Asset returned successfully"));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<AssetHistoryResponse>>> getAssetHistory(@PathVariable String id) {
        log.debug("GET /api/assets/{}/history", id);

        List<AssetHistoryResponse> history = assetHistoryService.getAssetHistory(id);

        return ResponseEntity.ok(ApiResponse.success(history));
    }

    // ===== BROKEN ASSET WORKFLOW =====

    /** User reports an asset they hold as broken. */
    @PostMapping("/{id}/report-broken")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<AssetResponse>> reportAssetBroken(@PathVariable String id) {
        String userId = securityHelper.getCurrentUserIdOrThrow();
        String performedBy = securityHelper.getCurrentUserEmailOrDefault("system");
        log.info("POST /api/assets/{}/report-broken - userId: {}", id, userId);

        AssetResponse asset = assetService.reportAssetBroken(id, userId, performedBy);

        return ResponseEntity.ok(ApiResponse.success(asset, "Asset reported as broken"));
    }

    /** Admin marks a broken asset as repaired and available. */
    @PostMapping("/{id}/mark-available")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AssetResponse>> markAssetAvailable(@PathVariable String id) {
        String performedBy = securityHelper.getCurrentUserEmailOrDefault("system");
        log.info("POST /api/assets/{}/mark-available - admin: {}", id, performedBy);

        AssetResponse asset = assetService.markAssetAvailable(id, performedBy);

        return ResponseEntity.ok(ApiResponse.success(asset, "Asset marked as available"));
    }
}
