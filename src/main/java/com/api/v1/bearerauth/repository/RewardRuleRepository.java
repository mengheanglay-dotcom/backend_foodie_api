package com.api.v1.bearerauth.repository;

import com.api.v1.bearerauth.model.entity.RewardRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRuleRepository extends JpaRepository<RewardRule, Long> {

    List<RewardRule> findByActiveTrue();
}