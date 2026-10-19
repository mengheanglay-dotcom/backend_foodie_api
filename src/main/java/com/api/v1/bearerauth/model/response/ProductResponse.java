package com.api.v1.bearerauth.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({
        "productId",
        "productName",
        "description",
        "rating",
        "unitPrice",
        "imageUrl",
        "categoryId",
        "categoryName"
})
public class ProductResponse {
    private Long productId;
    private String productName;
    private String description;
    private Double unitPrice;
    private Double rating;
    private String imageUrl;
    private Long categoryId;
    private String categoryName;
}
