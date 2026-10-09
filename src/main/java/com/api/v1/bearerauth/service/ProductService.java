package com.api.v1.bearerauth.service;

import com.api.v1.bearerauth.model.request.ProductRequest;
import com.api.v1.bearerauth.model.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface ProductService {
    Page<ProductResponse> list(int page, int size, Sort.Direction direction, String search, String category);
    ProductResponse get(Long id);
    java.util.List<String> categories();
    ProductResponse create(ProductRequest request);
    ProductResponse update(Long id, ProductRequest request);
    void delete(Long id);
}
