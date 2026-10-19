package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.Product;
import com.api.v1.bearerauth.model.entity.ProductOption;
import com.api.v1.bearerauth.model.enumerate.OptionType;
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

    @Override
    public ProductOptionResponse create(Long productId, ProductOptionRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Product not found"));

        validate(request);

        ProductOption option = new ProductOption();
        option.setProduct(product);
        option.setOptionName(request.getOptionName().trim());
        option.setOptionType(request.getOptionType());
        applyPrice(option, request);
        option.setActive(true);

        return toResponse(optionRepository.save(option));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductOptionResponse> findByProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Product not found");
        }

        return optionRepository.findByProduct_ProductIdAndActiveTrue(productId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public ProductOptionResponse update(Long optionId, ProductOptionRequest request) {
        ProductOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Option not found"));

        validate(request);
        option.setOptionName(request.getOptionName().trim());
        option.setOptionType(request.getOptionType());
        applyPrice(option, request);

        return toResponse(optionRepository.save(option));
    }

    @Override
    public void deactivate(Long optionId) {
        ProductOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Option not found"));
        option.setActive(false);
        optionRepository.save(option);
    }

    @Override
    public ProductOptionResponse activate(Long optionId) {
        ProductOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Option not found"));
        option.setActive(true);
        return toResponse(optionRepository.save(option));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductOptionResponse> findAll() {
        return optionRepository.findAll().stream().map(this::toResponse).toList();
    }

    private void validate(ProductOptionRequest request) {
        if (request.getOptionType() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Option type is required");
        }

        if (request.getOptionType() == OptionType.SIZE) {
            Double price = request.getPrice();
            if (price == null || !Double.isFinite(price) || price <= 0) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "A size must have a price greater than 0");
            }
        } else if (request.getOptionType() == OptionType.ADD_ON) {
            Double extraPrice = request.getExtraPrice();
            if (extraPrice == null || !Double.isFinite(extraPrice) || extraPrice < 0) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "An add-on price must be zero or greater");
            }
        }
    }

    private void applyPrice(ProductOption option, ProductOptionRequest request) {
        if (request.getOptionType() == OptionType.SIZE) {
            option.setPrice(request.getPrice());
            // The existing database column extra_price is NOT NULL; size prices are not additive.
            option.setExtraPrice(0.0);
        } else {
            option.setPrice(null);
            option.setExtraPrice(request.getExtraPrice());
        }
    }

    private ProductOptionResponse toResponse(ProductOption option) {
        return new ProductOptionResponse(
                option.getOptionId(),
                option.getOptionName(),
                option.getOptionType(),
                option.getPrice(),
                option.getExtraPrice(),
                option.getActive(),
                option.getProduct().getProductId(),
                option.getProduct().getProductName()
        );
    }
}
