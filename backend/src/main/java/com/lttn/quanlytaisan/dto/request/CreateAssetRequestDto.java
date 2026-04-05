package com.lttn.quanlytaisan.dto.request;

import com.lttn.quanlytaisan.model.AssetRequestCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssetRequestDto {

    /** User only selects category; admin sets concrete asset name when approving. */
    @NotNull(message = "Danh mục là bắt buộc")
    private AssetRequestCategory category;

    @NotBlank(message = "Lý do là bắt buộc")
    private String reason;

    private String note;
}
