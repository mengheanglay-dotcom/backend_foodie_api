package com.api.v1.bearerauth.service;

import com.api.v1.bearerauth.model.request.OrderRequest;
import com.api.v1.bearerauth.model.request.OrderStatusRequest;
import com.api.v1.bearerauth.model.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface OrderService {
    Page<OrderResponse> list(int page, int size, Sort.Direction direction);
    OrderResponse get(Long id);
    OrderResponse create(OrderRequest request);
    OrderResponse update(Long id, OrderRequest request);
    void delete(Long id);
    OrderResponse updateStatus(Long id, OrderStatusRequest request);
}
