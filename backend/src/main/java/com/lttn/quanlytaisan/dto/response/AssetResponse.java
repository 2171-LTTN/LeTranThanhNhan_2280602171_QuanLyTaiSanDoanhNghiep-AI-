package com.lttn.quanlytaisan.dto.response;

import com.lttn.quanlytaisan.model.AssetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetResponse {

    private String id;
    private String name;
    private String category;
    private AssetStatus status;
    private String assignedTo;
    private String assignedToName;
    private LocalDate purchaseDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
