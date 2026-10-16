package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.AppUser;
import com.api.v1.bearerauth.model.entity.AppUserToken;
import com.api.v1.bearerauth.repository.AppUserTokenRepository;
import com.api.v1.bearerauth.service.AppUserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AppUserTokenServiceImpl implements AppUserTokenService {

    private final AppUserTokenRepository tokenRepository;

    @Override
    public void save(AppUserToken token) {
        tokenRepository.save(token);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppUserToken> findAllByUser(AppUser user) {
        return tokenRepository.findAllByUser(user);
    }

    @Override
    @Transactional
    public void deleteAll(List<AppUserToken> tokens) {
        tokenRepository.deleteAll(tokens);
    }

    @Override
    @Transactional
    public String existsByRefresh(String refresh) {
        AppUserToken token = tokenRepository.findByRefresh(refresh).orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "Invalid or revoked refresh token"));

        if (!token.getExpiresAt().isAfter(LocalDateTime.now())) {
            tokenRepository.delete(token);

            throw new CustomException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }

        return token.getRefresh();
    }
}
