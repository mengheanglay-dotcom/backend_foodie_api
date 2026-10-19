package com.api.v1.bearerauth.repository;

import com.api.v1.bearerauth.model.entity.CustomerReward;
import com.api.v1.bearerauth.model.enumerate.RewardStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRewardRepository extends JpaRepository<CustomerReward, Long> {
    List<CustomerReward> findByCustomer_CustomerIdOrderByEarnedAtDesc(Long customerId);

    List<CustomerReward> findByCustomer_CustomerIdAndStatusOrderByExpiresAtAsc(
            Long customerId, RewardStatus status);

    boolean existsByCustomer_CustomerIdAndQualifyingOrder_OrderIdAndRewardRule_RewardRuleId(
            Long customerId, Long orderId, Long rewardRuleId);

    List<CustomerReward> findByRedemptionOrder_OrderId(Long orderId);
    Optional<CustomerReward> findFirstByRedemptionOrder_OrderId(Long orderId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from CustomerReward r where r.customerRewardId = :rewardId " +
            "and r.customer.customerId = :customerId")
    Optional<CustomerReward> findByIdAndCustomerForUpdate(
            @Param("rewardId") Long rewardId,
            @Param("customerId") Long customerId);
}