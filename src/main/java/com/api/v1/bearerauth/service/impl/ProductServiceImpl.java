package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.Product;
import com.api.v1.bearerauth.model.request.ProductRequest;
import com.api.v1.bearerauth.model.response.ProductResponse;
import com.api.v1.bearerauth.repository.ProductRepository;
import com.api.v1.bearerauth.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> list(int page, int size, Sort.Direction direction, String search, String category) {
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(direction, "productId"));
        String q = search == null ? "" : search.trim();
        String c = category == null ? "" : category.trim();
        if (!q.isEmpty() && !c.isEmpty()) {
            return productRepository.findByProductNameContainingIgnoreCaseAndCategoryIgnoreCase(q, c, pageable).map(product -> mapper.map(product, ProductResponse.class));
        }
        if (!q.isEmpty()) {
            return productRepository.findByProductNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(q, q, pageable).map(product -> mapper.map(product, ProductResponse.class));
        }
        if (!c.isEmpty()) {
            return productRepository.findByCategoryIgnoreCase(c, pageable).map(product -> mapper.map(product, ProductResponse.class));
        }
        return productRepository.findAll(pageable).map(product -> mapper.map(product, ProductResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<String> categories() {
        return productRepository.findAll().stream().map(Product::getCategory).filter(java.util.Objects::nonNull).distinct().sorted().toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse get(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Product not found"));
        return mapper.map(product, ProductResponse.class);
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        apply(product, request);
        return mapper.map(productRepository.save(product), ProductResponse.class);
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Product not found"));
        apply(product, request);
        return mapper.map(productRepository.save(product), ProductResponse.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }

    private void apply(Product product, ProductRequest request) {
        product.setProductName(request.getName().trim());
        product.setDescription(request.getSubtitle().trim());
        product.setUnitPrice(request.getPrice());
        product.setCategory(request.getCategory().trim());
        product.setImageUrl(request.getImageUrl().trim());
        product.setRating(request.getRating());
    }
}
