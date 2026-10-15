package com.api.v1.bearerauth.model.request;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PromoCreateRequest {

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Promo code can contain letters, numbers, _ and - only")
    private String code;

    @Size(max = 255)
    private String description;

    @NotNull
    @DecimalMin("0.01")
    @DecimalMax("1.00")
    private Double discountRate;

    @NotNull
    @DecimalMin("0.0")
    private Double minimumSubtotal = 0.0;

    private LocalDateTime startAt;

    private LocalDateTime endAt;
}
