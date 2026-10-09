package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.AppUser;
import com.api.v1.bearerauth.model.entity.Customer;
import com.api.v1.bearerauth.model.enumerate.Roles;
import com.api.v1.bearerauth.model.request.AppUserAddRequest;
import com.api.v1.bearerauth.model.request.AppUserUpdateRequest;
import com.api.v1.bearerauth.model.response.AppUserDto;
import com.api.v1.bearerauth.repository.AppUserRepository;
import com.api.v1.bearerauth.repository.CustomerRepository;
import com.api.v1.bearerauth.security.CurrentUserService;
import com.api.v1.bearerauth.service.AppUserService;
import com.api.v1.bearerauth.service.AppUserTokenService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final CustomerRepository customerRepository;
    private final AppUserTokenService tokenService;
    private final CurrentUserService currentUserService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public AppUserDto current() {
        return mapper.map(currentUserService.getCurrentUser(), AppUserDto.class);
    }

    @Override
    @Transactional
    public void logout() {
        AppUser user = currentUserService.getCurrentUser();
        tokenService.deleteAll(tokenService.findAllByUser(user));
        SecurityContextHolder.clearContext();
    }

    @Override
    @Transactional
    public AppUserDto create(AppUserAddRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (appUserRepository.findByEmail(email).isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "Email already exists");
        }
        if (!request.getPassword().equals(request.getConfirm())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Password and confirm password do not match");
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername().trim());
        user.setAddress(request.getAddress() == null ? "" : request.getAddress().trim());
        user.setPhone(request.getPhone().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Roles.USER);

        Customer customer = new Customer();
        customer.setCustomerName(user.getUsername());
        customer.setAddress(user.getAddress());
        customer.setPhoneNumber(user.getPhone());
        customer.setUser(user);
        user.setCustomer(customer);

        return mapper.map(appUserRepository.save(user), AppUserDto.class);
    }

    @Override
    @Transactional
    public AppUserDto update(AppUserUpdateRequest request) {
        AppUser user = currentUserService.getCurrentUser();
        if (!user.getEmail().equalsIgnoreCase(request.getEmail()) && appUserRepository.findByEmail(request.getEmail().toLowerCase()).isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "Email already exists");
        }

        user.setUsername(request.getUsername().trim());
        user.setAddress(request.getAddress() == null ? "" : request.getAddress().trim());
        user.setPhone(request.getPhone().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());

        Customer customer = customerRepository.findByUser(user).orElseGet(() -> {
            Customer c = new Customer();
            c.setUser(user);
            return c;
        });
        customer.setCustomerName(user.getUsername());
        customer.setAddress(user.getAddress());
        customer.setPhoneNumber(user.getPhone());
        customerRepository.save(customer);

        return mapper.map(appUserRepository.save(user), AppUserDto.class);
    }
}
