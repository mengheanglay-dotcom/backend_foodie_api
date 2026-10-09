package com.api.v1.bearerauth.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PromoResponse {
    private boolean valid;
    private String code;
    private double discount;
    private double discountRate;
    private String message;
}
