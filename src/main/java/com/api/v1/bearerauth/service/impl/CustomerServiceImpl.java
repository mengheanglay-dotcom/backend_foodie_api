package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.Customer;
import com.api.v1.bearerauth.model.response.CustomerResponse;
import com.api.v1.bearerauth.repository.CustomerRepository;
import com.api.v1.bearerauth.security.CurrentUserService;
import com.api.v1.bearerauth.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final CurrentUserService currentUserService;

    @Override
    public Page<CustomerResponse> list(int page, int size, Sort.Direction direction) {
        return repository.findAll(PageRequest.of(page - 1, size, Sort.by(direction, "customerId"))).map(this::toResponse);
    }

    @Override
    public CustomerResponse get(Long id) {
        Customer customer = repository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Customer not found"));
        return toResponse(customer);
    }

    @Override
    public CustomerResponse create(com.api.v1.bearerauth.model.request.CustomerRequest request) {
        throw new CustomException(HttpStatus.METHOD_NOT_ALLOWED, "Customers are created automatically during registration");
    }

    @Override
    public CustomerResponse update(Long id, com.api.v1.bearerauth.model.request.CustomerRequest request) {
        Customer customer = repository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Customer not found"));
        customer.setAddress(request.getAddress());
        customer.setCustomerName(request.getCustomerName());
        customer.setPhoneNumber(request.getPhoneNumber());
        return toResponse(repository.save(customer));
    }

    @Override
    public void delete(Long id) {
        throw new CustomException(HttpStatus.METHOD_NOT_ALLOWED, "Delete customer through user management");
    }

    private CustomerResponse toResponse(Customer c) {
        CustomerResponse r = new CustomerResponse();
        r.setCustomerId(c.getCustomerId());
        r.setAddress(c.getAddress());
        r.setCustomerName(c.getCustomerName());
        r.setPhoneNumber(c.getPhoneNumber());
        r.setEmail(c.getUser().getEmail());
        return r;
    }
}
