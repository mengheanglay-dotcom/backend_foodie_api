package com.api.v1.bearerauth.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductRequest {
    @NotBlank private String name;
    @NotBlank private String subtitle;
    @NotNull @DecimalMin("0.0") private Double price;
    @NotNull @DecimalMin("0.0") private Double rating;
    @NotNull private Long categoryId;
    @Schema(type = "string", format = "binary", description = "Product image file")
    private MultipartFile image;
}
