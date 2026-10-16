package com.api.v1.bearerauth.service;


import com.api.v1.bearerauth.model.entity.OrderTb;
import com.api.v1.bearerauth.model.entity.ProductOrder;
import com.api.v1.bearerauth.model.request.OrderItemRequest;

import java.util.List;

public interface OrderItemService {

    List<ProductOrder> createItems(OrderTb order, List<OrderItemRequest> requests);
}
