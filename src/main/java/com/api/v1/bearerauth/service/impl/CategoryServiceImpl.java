package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.Category;
import com.api.v1.bearerauth.model.entity.Product;
import com.api.v1.bearerauth.model.request.CategoryRequest;
import com.api.v1.bearerauth.model.response.CategoryResponse;
import com.api.v1.bearerauth.model.response.ProductResponse;
import com.api.v1.bearerauth.repository.CategoryRepository;
import com.api.v1.bearerauth.repository.ProductRepository;
import com.api.v1.bearerauth.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> list() {
        return categoryRepository.findAllWithProducts()
                .stream()
                .sorted(Comparator.comparing(Category::getCategoryName, String.CASE_INSENSITIVE_ORDER))
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse get(Long id) {
        Category category = categoryRepository.findByIdWithProducts(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        return toResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        categoryRepository.findByCategoryNameIgnoreCase(request.getCategoryName().trim())
                .ifPresent(category -> {
                    throw new CustomException(HttpStatus.CONFLICT, "Category already exists");
                });

        Category category = Category.builder()
                .categoryName(request.getCategoryName().trim())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

        return toResponse(categoryRepository.save(category));
    }


    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findCategory(id);

        categoryRepository.findByCategoryNameIgnoreCase(request.getCategoryName().trim())
                .filter(existingCategory -> !existingCategory.getCategoryId().equals(id))
                .ifPresent(existingCategory -> {
                    throw new CustomException(HttpStatus.CONFLICT, "Category already exists");
                });

        category.setCategoryName(request.getCategoryName().trim());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());

        return toResponse(categoryRepository.save(category));
    }


    @Override
    @Transactional
    public void delete(Long id) {
        Category category = findCategory(id);

        if (productRepository.existsByCategory(category)) {
            throw new CustomException(HttpStatus.CONFLICT, "Cannot delete category because it has products");
        }

        categoryRepository.delete(category);
    }


    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    private CategoryResponse toResponse(Category category) {
        List<ProductResponse> products = category.getProducts()
                .stream()
                .map(this::toProductResponse)
                .toList();

        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .productCount((long) products.size())
                .products(products)
                .build();
    }

    private ProductResponse toProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getProductId());
        response.setProductName(product.getProductName());
        response.setDescription(product.getDescription());
        response.setUnitPrice(product.getUnitPrice());
        response.setRating(product.getRating());
        response.setImageUrl(product.getImageUrl());

        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getCategoryId());
            response.setCategoryName(product.getCategory().getCategoryName());
        }

        return response;
    }


}
