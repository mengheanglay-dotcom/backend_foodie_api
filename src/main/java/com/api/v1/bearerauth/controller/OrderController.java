package com.api.v1.bearerauth.controller;

import com.api.v1.bearerauth.common.response.PaginationResponse;
import com.api.v1.bearerauth.common.response.SuccessResponse;
import com.api.v1.bearerauth.model.request.OrderRequest;
import com.api.v1.bearerauth.model.response.OrderResponse;
import com.api.v1.bearerauth.service.OrderService;
import com.api.v1.bearerauth.util.ApiResponseUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<PaginationResponse<List<OrderResponse>>> list(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        return ResponseEntity.ok(ApiResponseUtil.pagination(HttpStatus.OK, orderService.list(page, size, direction)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<OrderResponse>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, orderService.get(id)));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<OrderResponse>> create(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseUtil.success(HttpStatus.CREATED, orderService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<OrderResponse>> update(@PathVariable Long id, @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, orderService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
