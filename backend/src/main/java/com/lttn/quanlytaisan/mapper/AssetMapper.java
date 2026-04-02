package com.lttn.quanlytaisan.mapper;

import com.lttn.quanlytaisan.dto.response.AssetResponse;
import com.lttn.quanlytaisan.model.Asset;
import com.lttn.quanlytaisan.model.User;
import com.lttn.quanlytaisan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Shared mapper utility for Asset entity conversions.
 * Handles all asset-to-DTO transformations with user lookup.
 */
@Component
@RequiredArgsConstructor
public class AssetMapper {

    private final UserRepository userRepository;

    public AssetResponse toResponse(Asset asset) {
        if (asset == null) {
            return null;
        }

        return AssetResponse.builder()
                .id(asset.getId())
                .name(asset.getName())
                .category(asset.getCategory())
                .status(asset.getStatus())
                .assignedTo(asset.getAssignedTo())
                .assignedToName(resolveUserName(asset.getAssignedTo()))
                .purchaseDate(asset.getPurchaseDate())
                .createdAt(asset.getCreatedAt())
                .updatedAt(asset.getUpdatedAt())
                .build();
    }

    public AssetResponse toResponseWithoutUserLookup(Asset asset) {
        if (asset == null) {
            return null;
        }

        return AssetResponse.builder()
                .id(asset.getId())
                .name(asset.getName())
                .category(asset.getCategory())
                .status(asset.getStatus())
                .assignedTo(asset.getAssignedTo())
                .purchaseDate(asset.getPurchaseDate())
                .createdAt(asset.getCreatedAt())
                .updatedAt(asset.getUpdatedAt())
                .build();
    }

    private String resolveUserName(String userId) {
        if (userId == null || userId.isBlank()) {
            return null;
        }
        return userRepository.findById(userId)
                .map(User::getName)
                .orElse(null);
    }
}
