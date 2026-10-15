package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.AppUser;
import com.api.v1.bearerauth.model.entity.Customer;
import com.api.v1.bearerauth.model.request.CustomerRequest;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> list(int page, int size, Sort.Direction direction) {
        return repository.findAll(PageRequest.of(page - 1, size, Sort.by(direction, "customerId")))
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse get(Long id) {
        return toResponse(findCustomer(id));
    }

    @Override
    public CustomerResponse create(CustomerRequest request) {
        throw new CustomException(HttpStatus.METHOD_NOT_ALLOWED, "Create customer through user management");
    }

    @Override
    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = findCustomer(id);

        customer.setCustomerName(request.getCustomerName().trim());
        customer.setAddress(request.getAddress() == null ? "" : request.getAddress().trim());
        customer.setPhoneNumber(request.getPhoneNumber().trim());

        return toResponse(repository.save(customer));
    }

    @Override
    public void delete(Long id) {
        throw new CustomException(HttpStatus.METHOD_NOT_ALLOWED, "Delete customer through user management");
    }

    private Customer findCustomer(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    private CustomerResponse toResponse(Customer c) {
        CustomerResponse r = new CustomerResponse();
        r.setCustomerId(c.getCustomerId());
        r.setAddress(c.getAddress());
        r.setCustomerName(c.getCustomerName());
        r.setPhoneNumber(c.getPhoneNumber());

        if (c.getUser() != null) {
            r.setEmail(c.getUser().getEmail());
        }

        return r;
    }
}

