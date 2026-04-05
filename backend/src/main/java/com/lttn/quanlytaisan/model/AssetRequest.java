package com.lttn.quanlytaisan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "asset_requests")
public class AssetRequest {

    @Id
    private String id;

    @Indexed
    private String requestedByUserId;

    private String requestedByUserName;

    private String requestedByUserEmail;

    private String assetName;

    private String category;

    private String reason;

    private String note;

    @Builder.Default
    private RequestStatus status = RequestStatus.PENDING;

    private String reviewedBy;

    private String reviewedByName;

    private String reviewNote;

    private LocalDateTime reviewedAt;

    private String assetId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Version
    private Long version;
}
