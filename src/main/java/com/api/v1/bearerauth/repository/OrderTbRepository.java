package com.api.v1.bearerauth.repository;

import com.api.v1.bearerauth.model.entity.AppUser;
import com.api.v1.bearerauth.model.entity.OrderTb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTbRepository extends JpaRepository<OrderTb, Long> {
    Page<OrderTb> findByCustomerUser(AppUser user, Pageable pageable);
}
