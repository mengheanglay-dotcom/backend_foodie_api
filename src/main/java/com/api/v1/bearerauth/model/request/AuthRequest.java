package com.api.v1.bearerauth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "Email can not be blank")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password can not be blank")
    @Pattern(regexp = "^.{8,}$", message = "Password must be at least 8 characters long")
    private String password;
}
