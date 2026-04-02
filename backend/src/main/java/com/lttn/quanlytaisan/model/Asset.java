package com.lttn.quanlytaisan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "assets")
public class Asset {

    @Id
    private String id;

    @Builder.Default
    private String name = "";

    @Builder.Default
    private String category = "";

    @Builder.Default
    private AssetStatus status = AssetStatus.AVAILABLE;

    private String assignedTo;

    private LocalDate purchaseDate;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}
