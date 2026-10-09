package com.api.v1.bearerauth.controller;

import com.api.v1.bearerauth.common.response.PaginationResponse;
import com.api.v1.bearerauth.common.response.SuccessResponse;
import com.api.v1.bearerauth.model.request.CustomerRequest;
import com.api.v1.bearerauth.model.response.CustomerResponse;
import com.api.v1.bearerauth.service.CustomerService;
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
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<PaginationResponse<List<CustomerResponse>>> list(@RequestParam(defaultValue = "1") @Min(1) int page,
                                                                           @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
                                                                           @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        return ResponseEntity.ok(ApiResponseUtil.pagination(HttpStatus.OK, customerService.list(page, size, direction)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<CustomerResponse>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, customerService.get(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<CustomerResponse>> update(@PathVariable Long id, @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, customerService.update(id, request)));
    }
}
