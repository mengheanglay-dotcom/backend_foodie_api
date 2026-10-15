package com.api.v1.bearerauth.repository;


import com.api.v1.bearerauth.model.entity.Promo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PromoRepository extends JpaRepository<Promo, Long> {

    Optional<Promo> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCaseAndPromoIdNot(String code, Long promoId);

    List<Promo> findByActiveTrue();
}

