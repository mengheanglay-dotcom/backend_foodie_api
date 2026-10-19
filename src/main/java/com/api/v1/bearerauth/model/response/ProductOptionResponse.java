package com.api.v1.bearerauth.model.response;


import com.api.v1.bearerauth.model.enumerate.OptionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductOptionResponse {

    private Long optionId;
    private String optionName;
    private OptionType optionType;
    // SIZE = full selected-size price; ADD_ON = null.
    private Double price;
    // ADD_ON = additional price; SIZE = 0.
    private Double extraPrice;
    private Boolean active;
    private Long productId;
    private String productName;
}
