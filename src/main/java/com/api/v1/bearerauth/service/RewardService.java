package com.api.v1.bearerauth.service;

import com.api.v1.bearerauth.model.entity.Customer;
import com.api.v1.bearerauth.model.entity.OrderTb;
import com.api.v1.bearerauth.model.enumerate.OrderStatus;
import com.api.v1.bearerauth.model.request.RewardRuleRequest;
import com.api.v1.bearerauth.model.response.CustomerRewardResponse;
import com.api.v1.bearerauth.model.response.RewardRuleResponse;
import java.util.List;

public interface RewardService {
    List<RewardRuleResponse> listRules();
    RewardRuleResponse getRule(Long id);
    RewardRuleResponse createRule(RewardRuleRequest request);
    RewardRuleResponse updateRule(Long id, RewardRuleRequest request);
    RewardRuleResponse activateRule(Long id);
    RewardRuleResponse deactivateRule(Long id);

    List<CustomerRewardResponse> getMyRewards();
    CustomerRewardResponse getRewardUsedForOrder(Long orderId);

    // Reserves a customer's selected reward and returns the server-calculated discount amount.
    double reserveReward(Customer customer, Long rewardId, double foodSubtotal, OrderTb order);
    void releaseReservedRewardForOrder(OrderTb order);
    void handleOrderStatusChange(OrderTb order, OrderStatus previousStatus, OrderStatus newStatus);
}
