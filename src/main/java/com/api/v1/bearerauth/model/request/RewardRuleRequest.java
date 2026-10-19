package com.api.v1.bearerauth.model.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RewardRuleRequest {
    @NotBlank(message = "Reward rule name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @NotNull(message = "Minimum qualifying subtotal is required")
    @DecimalMin(value = "0.01", message = "Minimum qualifying subtotal must be greater than zero")
    private Double minimumQualifyingSubtotal;

    @NotNull(message = "Discount rate is required")
    @DecimalMin(value = "0.01", message = "Discount rate must be at least 1%")
    @DecimalMax(value = "1.00", message = "Discount rate cannot exceed 100%")
    private Double discountRate;

    @NotNull(message = "Minimum redemption subtotal is required")
    @DecimalMin(value = "0.00", message = "Minimum redemption subtotal cannot be negative")
    private Double minimumRedemptionSubtotal = 0.0;

    @NotNull(message = "Validity days is required")
    @Min(value = 1, message = "Validity must be at least 1 day")
    @Max(value = 3650, message = "Validity cannot exceed 3650 days")
    private Integer validityDays = 30;
}
