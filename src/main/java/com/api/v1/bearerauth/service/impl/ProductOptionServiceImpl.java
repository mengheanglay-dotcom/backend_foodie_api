package com.api.v1.bearerauth.service.impl;


import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.Product;
import com.api.v1.bearerauth.model.entity.ProductOption;
import com.api.v1.bearerauth.model.request.ProductOptionRequest;
import com.api.v1.bearerauth.model.response.ProductOptionResponse;
import com.api.v1.bearerauth.repository.ProductOptionRepository;
import com.api.v1.bearerauth.repository.ProductRepository;
import com.api.v1.bearerauth.service.ProductOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductOptionServiceImpl implements ProductOptionService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository optionRepository;
    private final ProductOptionRepository productOptionRepository;


    @Override
    public ProductOptionResponse create(Long productId, ProductOptionRequest request) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Product not found"));

        validate(request);

        ProductOption option = new ProductOption();
        option.setProduct(product);
        option.setOptionName(request.getOptionName().trim());
        option.setOptionType(request.getOptionType());
        option.setExtraPrice(request.getExtraPrice());
        option.setActive(true);

        return toResponse(optionRepository.save(option));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductOptionResponse> findByProduct(Long productId) {

        if (!productRepository.existsById(productId)) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Product not found");
        }

        return optionRepository.findByProduct_ProductIdAndActiveTrue(productId).stream().map(this::toResponse).toList();
    }

    @Override
    public ProductOptionResponse update(Long optionId, ProductOptionRequest request) {

        ProductOption option = optionRepository.findById(optionId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Option not found"));

        validate(request);

        option.setOptionName(request.getOptionName().trim());
        option.setOptionType(request.getOptionType());
        option.setExtraPrice(request.getExtraPrice());

        return toResponse(optionRepository.save(option));
    }

    @Override
    public void deactivate(Long optionId) {
        ProductOption option = optionRepository.findById(optionId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Option not found"));

        option.setActive(false);
        optionRepository.save(option);
    }

    @Override
    public ProductOptionResponse activate(Long optionId) {
        ProductOption option = optionRepository.findById(optionId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Option not found"));

        option.setActive(true);

        return toResponse(optionRepository.save(option));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductOptionResponse> findAll() {
        return productOptionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void validate(ProductOptionRequest request) {
        if (request.getExtraPrice() == null || !Double.isFinite(request.getExtraPrice()) || request.getExtraPrice() < 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Option price must be zero or greater");
        }
    }

    private ProductOptionResponse toResponse(ProductOption option) {

        return new ProductOptionResponse(
                option.getOptionId(),
                option.getOptionName(),
                option.getOptionType(),
                option.getExtraPrice(),
                option.getActive(),
                option.getProduct().getProductId(),
                option.getProduct().getProductName());
    }
}
