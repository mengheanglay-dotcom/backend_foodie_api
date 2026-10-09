package com.api.v1.bearerauth.service;

import com.api.v1.bearerauth.model.entity.AppUser;
import com.api.v1.bearerauth.model.entity.AppUserToken;

import java.util.List;

public interface AppUserTokenService {
    void save(AppUserToken token);
    List<AppUserToken> findAllByUser(AppUser user);
    void deleteAll(List<AppUserToken> tokens);
    String existsByRefresh(String refresh);
}
