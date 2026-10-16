package com.api.v1.bearerauth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class PromoValidateRequest {
    @NotBlank
    private String code;
    @NotNull
    @PositiveOrZero
    private Double subtotal;
}
