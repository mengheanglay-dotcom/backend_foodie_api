
package com.api.v1.bearerauth.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reward_rule")
public class RewardRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewardRuleId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    // Minimum food subtotal required to EARN this reward.
    @Column(nullable = false)
    private Double minimumQualifyingSubtotal;

    // 0.10 means 10% off the next eligible order.
    @Column(nullable = false)
    private Double discountRate;

    // Minimum food subtotal required to USE the reward.
    @Column(nullable = false)
    private Double minimumRedemptionSubtotal = 0.0;

    @Column(nullable = false)
    private Integer validityDays = 30;

    @Column(nullable = false)
    private Boolean active = true;
}
