package com.api.v1.bearerauth.model.response;

import lombok.Data;

@Data
public class RewardRuleResponse {
    private Long rewardRuleId;
    private String name;
    private String description;
    private Double minimumQualifyingSubtotal;
    private Double discountRate;
    private Double minimumRedemptionSubtotal;
    private Integer validityDays;
    private Boolean active;
}
