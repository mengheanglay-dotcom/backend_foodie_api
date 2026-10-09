package com.api.v1.bearerauth.service;

import com.api.v1.bearerauth.model.request.CustomerRequest;
import com.api.v1.bearerauth.model.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface CustomerService {
    Page<CustomerResponse> list(int page, int size, Sort.Direction direction);
    CustomerResponse get(Long id);
    CustomerResponse create(CustomerRequest request);
    CustomerResponse update(Long id, CustomerRequest request);
    void delete(Long id);
}
