package com.api.v1.bearerauth.model.entity;

import com.api.v1.bearerauth.model.enumerate.RewardStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customer_reward", uniqueConstraints = {
        @UniqueConstraint(name = "uk_customer_reward_earning",
                columnNames = {"customer_id", "qualifying_order_id", "reward_rule_id"}),
        @UniqueConstraint(name = "uk_customer_reward_redemption_order",
                columnNames = {"redemption_order_id"})
})
public class CustomerReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerRewardId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reward_rule_id", nullable = false)
    private RewardRule rewardRule;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qualifying_order_id", nullable = false)
    private OrderTb qualifyingOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "redemption_order_id")
    private OrderTb redemptionOrder;

    // Snapshot fields preserve the earned reward if Admin later edits a rule.
    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private Double discountRate;

    @Column(nullable = false)
    private Double minimumRedemptionSubtotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RewardStatus status = RewardStatus.AVAILABLE;

    @Column(nullable = false)
    private LocalDateTime earnedAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime redeemedAt;
}
