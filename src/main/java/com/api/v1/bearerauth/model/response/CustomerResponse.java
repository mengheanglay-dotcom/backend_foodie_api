package com.api.v1.bearerauth.model.response;

import lombok.Data;

@Data
public class CustomerResponse {
    private Long customerId;
    private String address;
    private String customerName;
    private String phoneNumber;
    private String email;
}
