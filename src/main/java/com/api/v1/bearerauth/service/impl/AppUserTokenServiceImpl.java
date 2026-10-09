package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.AppUser;
import com.api.v1.bearerauth.model.entity.AppUserToken;
import com.api.v1.bearerauth.repository.AppUserTokenRepository;
import com.api.v1.bearerauth.service.AppUserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserTokenServiceImpl implements AppUserTokenService {
    private final AppUserTokenRepository repository;

    @Override public void save(AppUserToken token) { repository.save(token); }
    @Override public List<AppUserToken> findAllByUser(AppUser user) { return repository.findAllByUser(user); }
    @Override public void deleteAll(List<AppUserToken> tokens) { repository.deleteAll(tokens); }

    @Override
    public String existsByRefresh(String refresh) {
        AppUserToken token = repository.findByRefresh(refresh)
                .orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));
        return token.getRefresh();
    }
}
