package com.api.v1.bearerauth.model.request;

import com.api.v1.bearerauth.model.enumerate.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusRequest {
    @NotNull(message = "Status is required")
    private OrderStatus status;
}
