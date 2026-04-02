package com.lttn.quanlytaisan.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssetRequest {

    @NotBlank(message = "Asset name is required")
    @Size(min = 1, max = 200, message = "Name must be between 1 and 200 characters")
    private String name;

    @NotBlank(message = "Category is required")
    @Size(min = 1, max = 100, message = "Category must be between 1 and 100 characters")
    private String category;

    private String purchaseDate;

    @Positive(message = "Purchase price must be positive")
    private Double purchasePrice;

    @NotBlank(message = "Serial number is required")
    @Size(min = 1, max = 100, message = "Serial number must be between 1 and 100 characters")
    private String serialNumber;

    @NotBlank(message = "Brand is required")
    @Size(min = 1, max = 100, message = "Brand must be between 1 and 100 characters")
    private String brand;

    @NotBlank(message = "Model is required")
    @Size(min = 1, max = 100, message = "Model must be between 1 and 100 characters")
    private String model;

    private String warrantyUntil;

    @NotBlank(message = "Location is required")
    @Size(min = 1, max = 200, message = "Location must be between 1 and 200 characters")
    private String location;

    private String note;
}
