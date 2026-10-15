package com.api.v1.bearerauth.controller;

import com.api.v1.bearerauth.common.response.SuccessResponse;
import com.api.v1.bearerauth.model.request.PromoCreateRequest;
import com.api.v1.bearerauth.model.request.PromoValidateRequest;
import com.api.v1.bearerauth.model.response.PromoDetailsResponse;
import com.api.v1.bearerauth.model.response.PromoResponse;
import com.api.v1.bearerauth.service.PromoService;
import com.api.v1.bearerauth.util.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/promos")
@RequiredArgsConstructor
public class PromoController {

    private final PromoService promoService;

    // Any authenticated user can validate a promo
    @PostMapping("/validate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse<PromoResponse>> validate(@Valid @RequestBody PromoValidateRequest request) {

        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, promoService.validate(request)));
    }

    // Authenticated users can see currently available promotions
    @GetMapping("/active")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse<List<PromoDetailsResponse>>> findActive() {

        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, promoService.findActive()));
    }

    // Admin can see all promotions, including inactive ones
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<PromoDetailsResponse>>> findAll() {

        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, promoService.findAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<PromoDetailsResponse>> getById(@PathVariable Long id) {

        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, promoService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<PromoDetailsResponse>> create(@Valid @RequestBody PromoCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseUtil.success(HttpStatus.CREATED, promoService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<PromoDetailsResponse>> update(@PathVariable Long id, @Valid @RequestBody PromoCreateRequest request) {

        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, promoService.update(id, request)));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<PromoDetailsResponse>> activate(@PathVariable Long id) {

        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, promoService.activate(id)));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<PromoDetailsResponse>> deactivate(@PathVariable Long id) {

        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, promoService.deactivate(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<String>> delete(@PathVariable Long id) {

        promoService.delete(id);

        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, "Promo deleted successfully"));
    }
}

