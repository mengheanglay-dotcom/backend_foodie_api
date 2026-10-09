package com.api.v1.bearerauth.repository;

import com.api.v1.bearerauth.model.entity.AppUser;
import com.api.v1.bearerauth.model.entity.Favorite;
import com.api.v1.bearerauth.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    List<Favorite> findAllByUserOrderByCreatedAtDesc(AppUser user);
    Optional<Favorite> findByUserAndProduct(AppUser user, Product product);
    boolean existsByUserAndProduct(AppUser user, Product product);
}
