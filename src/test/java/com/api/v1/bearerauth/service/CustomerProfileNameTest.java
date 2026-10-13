package com.api.v1.bearerauth.service;

import com.api.v1.bearerauth.model.entity.AppUser;
import com.api.v1.bearerauth.model.entity.Customer;
import com.api.v1.bearerauth.model.request.CustomerRequest;
import com.api.v1.bearerauth.model.response.AppUserDto;
import com.api.v1.bearerauth.repository.AppUserRepository;
import com.api.v1.bearerauth.repository.CustomerRepository;
import com.api.v1.bearerauth.security.CurrentUserService;
import com.api.v1.bearerauth.service.impl.AppUserServiceImpl;
import com.api.v1.bearerauth.service.impl.CustomerServiceImpl;
import com.api.v1.bearerauth.service.AppUserTokenService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerProfileNameTest {

    @Test
    void updatedCustomerNameIsReturnedByAppUserProfileWhileEmailRemainsLoginName() {
        String email = "tha@example.com";
        AppUser user = new AppUser();
        user.setId(1L);
        user.setEmail(email);

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setCustomerName("Old Name");
        customer.setAddress("Old address");
        customer.setPhoneNumber("012345678");
        customer.setUser(user);
        user.setCustomer(customer);

        CustomerRepository customerRepository = mock(CustomerRepository.class);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        CustomerRequest request = new CustomerRequest();
        request.setCustomerName("Updated Name");
        request.setAddress("New address");
        request.setPhoneNumber("012345678");
        new CustomerServiceImpl(customerRepository).update(1L, request);

        CurrentUserService currentUserService = mock(CurrentUserService.class);
        when(currentUserService.getCurrentUser()).thenReturn(user);
        AppUserServiceImpl appUserService = new AppUserServiceImpl(
                mock(AppUserRepository.class),
                customerRepository,
                mock(AppUserTokenService.class),
                currentUserService,
                mock(PasswordEncoder.class),
                mock(ModelMapper.class)
        );

        AppUserDto profile = appUserService.current();

        assertEquals("Updated Name", profile.getUsername());
        assertEquals(email, profile.getEmail());
        assertEquals(email, user.getUsername());
    }
}
