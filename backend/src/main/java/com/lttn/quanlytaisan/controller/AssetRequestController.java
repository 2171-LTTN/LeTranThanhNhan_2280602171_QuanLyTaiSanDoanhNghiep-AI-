package com.lttn.quanlytaisan.controller;

import com.lttn.quanlytaisan.dto.request.CreateAssetRequestDto;
import com.lttn.quanlytaisan.dto.request.ReviewAssetRequestDto;
import com.lttn.quanlytaisan.dto.response.ApiResponse;
import com.lttn.quanlytaisan.dto.response.AssetRequestResponse;
import com.lttn.quanlytaisan.service.AssetRequestService;
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
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@Slf4j
public class AssetRequestController {

    private final AssetRequestService assetRequestService;

    // ===== USER: Create & View My Requests =====

    @PostMapping("/me/allocation-requests")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<AssetRequestResponse>> createAllocationRequest(
            @Valid @RequestBody CreateAssetRequestDto dto
    ) {
        log.info("POST /api/requests/me/allocation-requests - category: {}", dto.getCategory());

        AssetRequestResponse response = assetRequestService.createRequest(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Request submitted successfully"));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<AssetRequestResponse>>> getMyRequests() {
        log.debug("GET /api/requests/my");

        List<AssetRequestResponse> requests = assetRequestService.getMyRequests();

        return ResponseEntity.ok(ApiResponse.success(requests));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<AssetRequestResponse>> cancelRequest(@PathVariable String id) {
        log.info("POST /api/requests/{}/cancel", id);

        AssetRequestResponse response = assetRequestService.cancelRequest(id);

        return ResponseEntity.ok(ApiResponse.success(response, "Request cancelled successfully"));
    }

    // ===== ADMIN: Manage All Requests =====

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<AssetRequestResponse>>> getAllRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status
    ) {
        log.debug("GET /api/requests - page: {}, size: {}, status: {}", page, size, status);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<AssetRequestResponse> requests;
        if (status != null && !status.isBlank()) {
            requests = assetRequestService.getPendingRequests(pageable);
        } else {
            requests = assetRequestService.getAllRequests(pageable);
        }

        return ResponseEntity.ok(ApiResponse.success(requests));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AssetRequestResponse>> getRequestById(@PathVariable String id) {
        log.debug("GET /api/requests/{}", id);

        AssetRequestResponse response = assetRequestService.getRequestById(id);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AssetRequestResponse>> approveRequest(
            @PathVariable String id,
            @RequestBody ReviewAssetRequestDto dto
    ) {
        log.info("POST /api/requests/{}/approve", id);

        AssetRequestResponse response = assetRequestService.approveRequest(id, dto);

        return ResponseEntity.ok(ApiResponse.success(response, "Request approved successfully"));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AssetRequestResponse>> rejectRequest(
            @PathVariable String id,
            @RequestBody ReviewAssetRequestDto dto
    ) {
        log.info("POST /api/requests/{}/reject", id);

        AssetRequestResponse response = assetRequestService.rejectRequest(id, dto);

        return ResponseEntity.ok(ApiResponse.success(response, "Request rejected successfully"));
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getRequestStats() {
        log.debug("GET /api/requests/stats");

        long pending = assetRequestService.countPendingRequests();

        Map<String, Long> stats = Map.of("pendingRequests", pending);

        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/my-stats")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getMyRequestStats() {
        log.debug("GET /api/requests/my-stats");

        long myPending = assetRequestService.countMyPendingRequests();

        Map<String, Long> stats = Map.of("myPendingRequests", myPending);

        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
