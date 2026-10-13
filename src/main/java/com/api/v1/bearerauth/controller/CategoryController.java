package com.api.v1.bearerauth.controller;

import com.api.v1.bearerauth.common.response.SuccessResponse;
import com.api.v1.bearerauth.model.request.CategoryRequest;
import com.api.v1.bearerauth.model.response.CategoryResponse;
import com.api.v1.bearerauth.service.CategoryService;
import com.api.v1.bearerauth.util.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<CategoryResponse>>> list() {
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, categoryService.list()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<CategoryResponse>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, categoryService.get(id)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<CategoryResponse>> create(@Valid @ModelAttribute CategoryRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseUtil.success(HttpStatus.CREATED, categoryService.create(request)));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute CategoryRequest request
    ) {
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, categoryService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}