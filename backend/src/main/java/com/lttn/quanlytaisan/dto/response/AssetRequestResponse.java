package com.lttn.quanlytaisan.dto.response;

import com.lttn.quanlytaisan.model.AssetRequest;
import com.lttn.quanlytaisan.model.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetRequestResponse {

    private String id;
    private String requestedByUserId;
    private String requestedByUserName;
    private String requestedByUserEmail;
    private String assetName;
    private String category;
    private String reason;
    private String note;
    private RequestStatus status;
    private String reviewedBy;
    private String reviewedByName;
    private String reviewNote;
    private LocalDateTime reviewedAt;
    private String assetId;
    private LocalDateTime createdAt;

    public static AssetRequestResponse fromEntity(AssetRequest entity) {
        return AssetRequestResponse.builder()
                .id(entity.getId())
                .requestedByUserId(entity.getRequestedByUserId())
                .requestedByUserName(entity.getRequestedByUserName())
                .requestedByUserEmail(entity.getRequestedByUserEmail())
                .assetName(entity.getAssetName())
                .category(entity.getCategory())
                .reason(entity.getReason())
                .note(entity.getNote())
                .status(entity.getStatus())
                .reviewedBy(entity.getReviewedBy())
                .reviewedByName(entity.getReviewedByName())
                .reviewNote(entity.getReviewNote())
                .reviewedAt(entity.getReviewedAt())
                .assetId(entity.getAssetId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
