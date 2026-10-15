package com.api.v1.bearerauth.model.response;

import com.api.v1.bearerauth.model.enumerate.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto {
    private Long id;
    private String email;
    private Roles role;

    private Long customerId;
    private String username;
}
