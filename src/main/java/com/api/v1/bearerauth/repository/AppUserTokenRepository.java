package com.api.v1.bearerauth.repository;

import com.api.v1.bearerauth.model.entity.AppUser;
import com.api.v1.bearerauth.model.entity.AppUserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserTokenRepository extends JpaRepository<AppUserToken, Long> {
    List<AppUserToken> findAllByUser(AppUser user);
    Optional<AppUserToken> findByRefresh(String refresh);
}
