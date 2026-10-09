package com.api.v1.bearerauth.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderItemResponse {
    @JsonProperty("id") private Long id;
    @JsonProperty("productId") private Long productId;
    @JsonProperty("name") private String productName;
    @JsonProperty("unitPrice") private Double unitPrice;
    @JsonProperty("quantity") private Integer quantity;
    @JsonProperty("size") private String size;
    @JsonProperty("addOns") private List<String> addOns = new ArrayList<>();
    @JsonProperty("subtotal") private Double subtotal;
}
