package com.api.v1.bearerauth.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank private String name;
    @NotBlank private String subtitle;
    @NotNull @DecimalMin("0.0") private Double price;
    @NotNull @DecimalMin("0.0") private Double rating;
    @NotBlank private String imageUrl;
    @NotBlank private String category;
}
