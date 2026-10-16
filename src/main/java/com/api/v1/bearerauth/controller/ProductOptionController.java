package com.api.v1.bearerauth.controller;


import com.api.v1.bearerauth.model.request.ProductOptionRequest;
import com.api.v1.bearerauth.model.response.ProductOptionResponse;
import com.api.v1.bearerauth.service.ProductOptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/{productId}/options")
@RequiredArgsConstructor
public class ProductOptionController {

    private final ProductOptionService optionService;

    @GetMapping
    public List<ProductOptionResponse> findByProduct(@PathVariable Long productId) {

        return optionService.findByProduct(productId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProductOptionResponse create(@PathVariable Long productId, @Valid @RequestBody ProductOptionRequest request) {

        return optionService.create(productId, request);
    }

}
