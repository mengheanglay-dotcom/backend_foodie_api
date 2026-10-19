package com.api.v1.bearerauth.controller;

import com.api.v1.bearerauth.common.response.SuccessResponse;
import com.api.v1.bearerauth.model.response.CustomerRewardResponse;
import com.api.v1.bearerauth.service.RewardService;
import com.api.v1.bearerauth.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rewards")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CustomerRewardController {
    private final RewardService rewardService;

    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<List<CustomerRewardResponse>>> myRewards() {
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, rewardService.getMyRewards()));
    }
}
