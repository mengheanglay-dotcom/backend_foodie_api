package com.api.v1.bearerauth.repository;

import com.api.v1.bearerauth.model.entity.Category;
import com.api.v1.bearerauth.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsByCategory(Category category);
    long countByCategory(Category category);
    Page<Product> findAllByCategoryCategoryNameContainingIgnoreCase(String categoryName, Pageable pageable);
}
