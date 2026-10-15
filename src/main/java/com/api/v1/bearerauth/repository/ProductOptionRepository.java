package com.api.v1.bearerauth.repository;


import com.api.v1.bearerauth.model.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    List<ProductOption> findByProduct_ProductIdAndActiveTrue(Long productId);

    List<ProductOption> findByProduct_ProductId(Long productId);

    List<ProductOption> findAllBy();
}
