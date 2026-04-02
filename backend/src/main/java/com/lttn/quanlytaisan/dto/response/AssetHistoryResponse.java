package com.lttn.quanlytaisan.dto.response;

import com.lttn.quanlytaisan.model.AssetAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetHistoryResponse {

    private String id;
    private String assetId;
    private String assetName;
    private String userId;
    private String userName;
    private String performedBy;
    private AssetAction action;
    private String details;
    private LocalDateTime timestamp;
}
