package com.api.v1.bearerauth.repository;


import com.api.v1.bearerauth.model.entity.ProductOrderOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderOptionRepository extends JpaRepository<ProductOrderOption, Long> {
}
