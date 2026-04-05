package com.lttn.quanlytaisan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAssetRequestDto {

    private String reviewNote;

    private String assetId;
}
