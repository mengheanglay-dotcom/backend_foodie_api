package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.Product;
import com.api.v1.bearerauth.model.entity.Category;
import com.api.v1.bearerauth.model.request.ProductRequest;
import com.api.v1.bearerauth.model.response.ProductResponse;
import com.api.v1.bearerauth.repository.CategoryRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> list(int page, int size, Sort.Direction direction, String search, String category) {
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(direction, "productId"));
        String q = search == null ? "" : search.trim();
        String c = category == null ? "" : category.trim();
        if (!q.isEmpty() && !c.isEmpty()) {
            return productRepository.findByProductNameAndCategoryName(q, c, pageable).map(this::toResponse);
        }
        if (!q.isEmpty()) {
            return productRepository.findByProductNameOrCategoryName(q, q, pageable).map(this::toResponse);
        }
        if (!c.isEmpty()) {
            return productRepository.findByCategoryName(c, pageable).map(this::toResponse);
        }
        return productRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse get(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Product not found"));
        return toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        apply(product, request, true);
        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Product not found"));
        apply(product, request, false);
        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }

    private void apply(Product product, ProductRequest request, boolean imageRequired) {
        product.setProductName(request.getName().trim());
        product.setDescription(request.getSubtitle().trim());
        product.setUnitPrice(request.getPrice());
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "Category not found"));
        product.setCategory(category);
        if (imageRequired && (request.getImage() == null || request.getImage().isEmpty())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Product image is required");
        }
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            product.setImageUrl(storeImage(request.getImage()));
        }
        product.setRating(request.getRating());
    }

    private String storeImage(MultipartFile image) {
        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Only image files are allowed");
        }
        String originalName = image.getOriginalFilename();
        String extension = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf('.')).toLowerCase()
                : "";
        try {
            Path directory = Paths.get("uploads", "products");
            Files.createDirectories(directory);
            String fileName = UUID.randomUUID() + extension;
            Files.copy(image.getInputStream(), directory.resolve(fileName));
            return "/uploads/products/" + fileName;
        } catch (IOException exception) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to store product image");
        }
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse response = mapper.map(product, ProductResponse.class);
        response.setCategory(product.getCategory().getName());
        response.setCategoryId(product.getCategory().getCategoryId());
        return response;
    }
}
