package com.api.v1.bearerauth.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class OrderItemResponse {

    private Long id;
    private Long productId;
    private String name;

    private Integer quantity;
    private Double unitPrice;

    // Selected size, for example: "Large"
    private String size;

    // Selected add-ons, for example: ["Cheese", "Bacon"]
    private List<String> addOns = new ArrayList<>();

    // Includes the base price and option prices × quantity
    private Double subtotal;
}

