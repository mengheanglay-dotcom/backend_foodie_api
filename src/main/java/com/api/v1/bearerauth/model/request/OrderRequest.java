package com.api.v1.bearerauth.model.request;

import com.api.v1.bearerauth.model.enumerate.OrderType;
import com.api.v1.bearerauth.model.enumerate.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    @NotNull(message = "Order type is required")
    private OrderType orderType;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String deliveryAddress;

    private String promoCode;

    @NotEmpty(message = "Order must contain at least one product")
    @Valid
    private List<OrderItemRequest> items;
}
