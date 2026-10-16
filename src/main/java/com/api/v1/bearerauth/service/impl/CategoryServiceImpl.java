package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.model.entity.Category;
import com.api.v1.bearerauth.model.entity.Product;
import com.api.v1.bearerauth.model.request.CategoryRequest;
import com.api.v1.bearerauth.model.response.CategoryResponse;
import com.api.v1.bearerauth.model.response.ProductResponse;
import com.api.v1.bearerauth.repository.CategoryRepository;
import com.api.v1.bearerauth.repository.ProductRepository;
import com.api.v1.bearerauth.service.CategoryService;
import com.api.v1.bearerauth.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

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
        String categoryName = request.getCategoryName() == null ? "" : request.getCategoryName().trim();
        categoryRepository.findByNameIgnoreCase(categoryName)
                .ifPresent(category -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Category already exists");
                });

        String imageUrl = null;
        if (hasImage(request.getImage())) {
            imageUrl = fileStorageService.store(request.getImage(), "categories");
        }

        Category category = Category.builder()
                .name(categoryName)
                .description(request.getDescription())
                .imageUrl(imageUrl)
                .build();

        return toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findCategory(id);
        String categoryName = request.getCategoryName() == null ? "" : request.getCategoryName().trim();

        categoryRepository.findByNameIgnoreCase(categoryName)
                .filter(existingCategory -> !existingCategory.getCategoryId().equals(id))
                .ifPresent(existingCategory -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Category already exists");
                });

        String oldImageUrl = category.getImageUrl();
        String newImageUrl = null;

        category.setCategoryName(categoryName);
        category.setDescription(request.getDescription());

        if (hasImage(request.getImage())) {
            newImageUrl = fileStorageService.store(request.getImage(), "categories");
            category.setImageUrl(newImageUrl);
        }

        Category savedCategory = categoryRepository.save(category);

        if (newImageUrl != null && oldImageUrl != null && !oldImageUrl.equals(newImageUrl)) {
            fileStorageService.delete(oldImageUrl);
        }

        return toResponse(savedCategory);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = findCategory(id);
        if (productRepository.existsByCategory(category)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete category because it has products");
        }

        String imageUrl = category.getImageUrl();
        categoryRepository.delete(category);

        if (imageUrl != null) {
            fileStorageService.delete(imageUrl);
        }
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    private boolean hasImage(MultipartFile image) {
        return image != null && !image.isEmpty();
    }

    private CategoryResponse toResponse(Category category) {
        List<Product> categoryProducts = category.getProducts() == null
                ? Collections.emptyList()
                : category.getProducts();

        List<ProductResponse> products = categoryProducts.stream()
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
