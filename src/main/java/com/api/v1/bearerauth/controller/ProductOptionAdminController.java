package com.api.v1.bearerauth.controller;


import com.api.v1.bearerauth.model.request.ProductOptionRequest;
import com.api.v1.bearerauth.model.response.ProductOptionResponse;
import com.api.v1.bearerauth.service.ProductOptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product-options")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ProductOptionAdminController {

    private final ProductOptionService optionService;
    private final ProductOptionService productOptionService;

    @PutMapping("/{optionId}")
    public ProductOptionResponse update(@PathVariable Long optionId, @Valid @RequestBody ProductOptionRequest request) {

        return optionService.update(optionId, request);
    }

    @PatchMapping("/{optionId}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long optionId) {

        optionService.deactivate(optionId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{optionId}/activate")
    public ProductOptionResponse activate(@PathVariable Long optionId) {

        return optionService.activate(optionId);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(productOptionService.findAll());
    }

}
