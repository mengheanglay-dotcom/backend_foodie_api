package com.api.v1.bearerauth.controller;

import com.api.v1.bearerauth.common.response.SuccessResponse;
import com.api.v1.bearerauth.model.response.FavoriteResponse;
import com.api.v1.bearerauth.service.FavoriteService;
import com.api.v1.bearerauth.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService service;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<FavoriteResponse>>> list() {
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, service.list()));
    }

    @GetMapping("/ids")
    public ResponseEntity<SuccessResponse<List<String>>> ids() {
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, service.ids()));
    }

    @PostMapping("/{productId}/toggle")
    public ResponseEntity<SuccessResponse<Boolean>> toggle(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, service.toggle(productId)));
    }
}
