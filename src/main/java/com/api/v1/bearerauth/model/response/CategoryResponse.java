package com.api.v1.bearerauth.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CategoryResponse {
    private Long categoryId;
    private String categoryName;
    private String description;
    private String imageUrl;
    private Long productCount;
    private List<ProductResponse> products;
}
