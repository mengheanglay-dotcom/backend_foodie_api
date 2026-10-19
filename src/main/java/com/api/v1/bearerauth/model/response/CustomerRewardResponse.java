package com.api.v1.bearerauth.model.response;

import com.api.v1.bearerauth.model.enumerate.RewardStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerRewardResponse {
    private Long rewardId;
    private Long rewardRuleId;
    private Long qualifyingOrderId;
    private Long redemptionOrderId;
    private String title;
    private Double discountRate;
    private Double minimumRedemptionSubtotal;
    private RewardStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime earnedAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiresAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime redeemedAt;
}
