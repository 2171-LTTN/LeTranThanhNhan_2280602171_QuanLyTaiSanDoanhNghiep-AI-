package com.lttn.quanlytaisan.controller;

import com.lttn.quanlytaisan.dto.request.AssignAssetRequest;
import com.lttn.quanlytaisan.dto.request.CreateAssetRequest;
import com.lttn.quanlytaisan.dto.request.UpdateAssetRequest;
import com.lttn.quanlytaisan.dto.response.ApiResponse;
import com.lttn.quanlytaisan.dto.response.AssetHistoryResponse;
import com.lttn.quanlytaisan.dto.response.AssetResponse;
import com.lttn.quanlytaisan.model.AssetStatus;
import com.lttn.quanlytaisan.security.JwtUtil;
import com.lttn.quanlytaisan.service.AssetHistoryService;
import com.lttn.quanlytaisan.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AssetController {

    private final AssetService assetService;
    private final AssetHistoryService assetHistoryService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AssetResponse>>> getAllAssets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) AssetStatus status,
            @RequestParam(required = false) String userId
    ) {
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetResponse>> getAssetById(@PathVariable String id) {
        try {
            AssetResponse asset = assetService.getAssetById(id);
            return ResponseEntity.ok(ApiResponse.success(asset));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AssetResponse>> createAsset(
            @Valid @RequestBody CreateAssetRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String performedBy = extractEmail(authHeader);
            AssetResponse asset = assetService.createAsset(request, performedBy);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(asset, "Asset created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetResponse>> updateAsset(
            @PathVariable String id,
            @Valid @RequestBody UpdateAssetRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String performedBy = extractEmail(authHeader);
            AssetResponse asset = assetService.updateAsset(id, request, performedBy);
            return ResponseEntity.ok(ApiResponse.success(asset, "Asset updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAsset(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String performedBy = extractEmail(authHeader);
            assetService.deleteAsset(id, performedBy);
            return ResponseEntity.ok(ApiResponse.success(null, "Asset deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<ApiResponse<AssetResponse>> assignAsset(
            @PathVariable String id,
            @Valid @RequestBody AssignAssetRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String performedBy = extractEmail(authHeader);
            AssetResponse asset = assetService.assignAsset(id, request, performedBy);
            return ResponseEntity.ok(ApiResponse.success(asset, "Asset assigned successfully"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<ApiResponse<AssetResponse>> returnAsset(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String performedBy = extractEmail(authHeader);
            AssetResponse asset = assetService.returnAsset(id, performedBy);
            return ResponseEntity.ok(ApiResponse.success(asset, "Asset returned successfully"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<AssetHistoryResponse>>> getAssetHistory(@PathVariable String id) {
        List<AssetHistoryResponse> history = assetHistoryService.getAssetHistory(id);
        return ResponseEntity.ok(ApiResponse.success(history));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getAssetStats() {
        Map<String, Long> stats = assetService.getAssetStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    private String extractEmail(String authHeader) {
        try {
            String token = authHeader.substring(7);
            return jwtUtil.extractUsername(token);
        } catch (Exception e) {
            return "system";
        }
    }
}
