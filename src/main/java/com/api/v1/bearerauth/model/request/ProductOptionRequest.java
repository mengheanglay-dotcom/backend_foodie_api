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

    // Required when optionType = SIZE. This is the full price for the selected size.
    @DecimalMin(value = "0.0", inclusive = false, message = "Size price must be greater than 0")
    private Double price;

    // Required when optionType = ADD_ON. This is added on top of the selected size price.
    @DecimalMin(value = "0.0", message = "Add-on price must be zero or greater")
    private Double extraPrice;
}

