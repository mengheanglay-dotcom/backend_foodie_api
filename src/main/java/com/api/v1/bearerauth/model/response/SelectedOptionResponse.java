package com.api.v1.bearerauth.model.response;


import com.api.v1.bearerauth.model.enumerate.OptionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelectedOptionResponse {
    private String optionName;
    private OptionType optionType;
    private Double extraPrice;
}
