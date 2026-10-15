package com.api.v1.bearerauth.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerRequest {
    @NotBlank
    private String address;
    @NotBlank
    private String customerName;
    @NotBlank
    private String phoneNumber;
}
