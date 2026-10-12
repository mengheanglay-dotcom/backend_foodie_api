package com.api.v1.bearerauth.repository;

import com.api.v1.bearerauth.model.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);
    @EntityGraph(attributePaths = "products")
    @Query("select c from Category c")
    List<Category> findAllWithProducts();

    @EntityGraph(attributePaths = "products")
    @Query("select c from Category c where c.categoryId = :id")
    Optional<Category> findByIdWithProducts(@Param("id") Long id);

}

