package com.api.v1.bearerauth.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductResponse {
    @JsonProperty("id") private Long productId;
    @JsonProperty("name") private String productName;
    @JsonProperty("subtitle") private String description;
    @JsonProperty("price") private Double unitPrice;
    @JsonProperty("rating") private Double rating;
    @JsonProperty("imageUrl") private String imageUrl;
    @JsonProperty("category") private String category;
    @JsonProperty("categoryId") private Long categoryId;
}
