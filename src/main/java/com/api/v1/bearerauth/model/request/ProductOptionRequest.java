package com.api.v1.bearerauth.model.request;

import com.api.v1.bearerauth.model.enumerate.OptionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductOptionRequest {

    @NotBlank
    private String optionName;

    @NotNull
    private OptionType optionType;

    @NotNull
    @DecimalMin("0.0")
    private Double extraPrice;
}

