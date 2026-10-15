package com.api.v1.bearerauth.service;


import com.api.v1.bearerauth.model.request.ProductOptionRequest;
import com.api.v1.bearerauth.model.response.ProductOptionResponse;

import java.util.List;

public interface ProductOptionService {

    ProductOptionResponse create(Long productId, ProductOptionRequest request);

    List<ProductOptionResponse> findByProduct(Long productId);

    List<ProductOptionResponse> findAll();

    ProductOptionResponse update(Long optionId, ProductOptionRequest request);

    ProductOptionResponse activate(Long optionId);

    void deactivate(Long optionId);
}
