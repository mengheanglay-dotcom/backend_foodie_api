package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.Category;
import com.api.v1.bearerauth.model.entity.Product;
import com.api.v1.bearerauth.model.request.ProductRequest;
import com.api.v1.bearerauth.model.response.ProductResponse;
import com.api.v1.bearerauth.repository.CategoryRepository;
import com.api.v1.bearerauth.repository.ProductRepository;
import com.api.v1.bearerauth.service.FileStorageService;
import com.api.v1.bearerauth.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> list(int page, int size, Sort.Direction direction,String search, String category) {

        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(direction, "productId"));

        String q = search == null ? "" : search.trim().toLowerCase();
        String c = category == null ? "" : category.trim().toLowerCase();

        return productRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);

            if (!q.isEmpty()) {
                Predicate productNameLike = cb.like(cb.lower(root.get("productName")), "%" + q + "%");
                Predicate categoryNameLike = cb.like(cb.lower(categoryJoin.get("categoryName")), "%" + q + "%");

                predicates.add(cb.or(productNameLike, categoryNameLike));
            }

            if (!c.isEmpty()) {
                predicates.add(cb.equal(cb.lower(categoryJoin.get("categoryName")), c));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse get(Long id) {
        Product product = findProduct(id);
        return toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        apply(product, request);

        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findProduct(id);

        apply(product, request);

        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = findProduct(id);
        productRepository.delete(product);
    }


    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    private void apply(Product product, ProductRequest request) {
        Category category = findCategory(request.getCategoryId());
        String imageUrl = fileStorageService.store(request.getImage(), "products");
        product.setProductName(request.getProductName().trim());
        product.setDescription(request.getDescription().trim());
        product.setUnitPrice(request.getUnitPrice());
        product.setImageUrl(imageUrl);
        if (imageUrl != null) {
            product.setImageUrl(imageUrl);
        }
        product.setRating(request.getRating());
        product.setCategory(category);
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse response = mapper.map(product, ProductResponse.class);

        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getCategoryId());
            response.setCategoryName(product.getCategory().getCategoryName());
        }

        return response;
    }
}
