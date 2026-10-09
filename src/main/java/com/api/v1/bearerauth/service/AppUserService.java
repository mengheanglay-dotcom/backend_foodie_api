package com.api.v1.bearerauth.service;

import com.api.v1.bearerauth.model.request.AppUserAddRequest;
import com.api.v1.bearerauth.model.request.AppUserUpdateRequest;
import com.api.v1.bearerauth.model.response.AppUserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {
    AppUserDto current();
    void logout();
    AppUserDto create(AppUserAddRequest request);
    AppUserDto update(AppUserUpdateRequest request);
}
