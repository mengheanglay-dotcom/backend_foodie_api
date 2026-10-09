package com.api.v1.bearerauth.repository;

import com.api.v1.bearerauth.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where lower(p.productName) like lower(concat('%', :name, '%')) or lower(p.category.name) like lower(concat('%', :category, '%'))")
    Page<Product> findByProductNameOrCategoryName(String name, String category, Pageable pageable);

    @Query("select p from Product p where lower(p.category.name) = lower(:category)")
    Page<Product> findByCategoryName(String category, Pageable pageable);

    @Query("select p from Product p where lower(p.productName) like lower(concat('%', :name, '%')) and lower(p.category.name) = lower(:category)")
    Page<Product> findByProductNameAndCategoryName(String name, String category, Pageable pageable);
}
