package com.api.v1.bearerauth.service;

import com.api.v1.bearerauth.model.request.CategoryRequest;
import com.api.v1.bearerauth.model.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> list();

    CategoryResponse get(Long id);

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);
}
