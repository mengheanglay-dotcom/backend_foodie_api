package com.api.v1.bearerauth.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token can not be blank")
    private String refresh;
}
