package com.api.v1.bearerauth.controller;

import com.api.v1.bearerauth.common.response.SuccessResponse;
import com.api.v1.bearerauth.model.request.PromoValidateRequest;
import com.api.v1.bearerauth.model.response.PromoResponse;
import com.api.v1.bearerauth.service.PromoService;
import com.api.v1.bearerauth.util.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/promos")
@RequiredArgsConstructor
public class PromoController {
    private final PromoService promoService;

    @PostMapping("/validate")
    public ResponseEntity<SuccessResponse<PromoResponse>> validate(@Valid @RequestBody PromoValidateRequest request) {
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, promoService.validate(request)));
    }
}
