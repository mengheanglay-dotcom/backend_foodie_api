package com.api.v1.bearerauth.service;

import com.api.v1.bearerauth.model.request.CreateProductRequest;
import com.api.v1.bearerauth.model.request.UpdateProductRequest;
import com.api.v1.bearerauth.model.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface ProductService {
    Page<ProductResponse> list(int page, int size, Sort.Direction direction, String search, String category);

    ProductResponse get(Long id);

    ProductResponse create(CreateProductRequest request);

    ProductResponse update(Long id, UpdateProductRequest request);

    void delete(Long id);
}
