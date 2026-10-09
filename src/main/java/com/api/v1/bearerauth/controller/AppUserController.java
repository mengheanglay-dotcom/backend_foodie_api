package com.api.v1.bearerauth.controller;

import com.api.v1.bearerauth.common.response.SuccessResponse;
import com.api.v1.bearerauth.model.request.AppUserUpdateRequest;
import com.api.v1.bearerauth.model.response.AppUserDto;
import com.api.v1.bearerauth.service.AppUserService;
import com.api.v1.bearerauth.util.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("/current")
    public ResponseEntity<SuccessResponse<AppUserDto>> current() {
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, appUserService.current()));
    }

    @PutMapping
    public ResponseEntity<SuccessResponse<AppUserDto>> update(@Valid @RequestBody AppUserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK, appUserService.update(request)));
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<Void>> logout() {
        appUserService.logout();
        return ResponseEntity.ok(ApiResponseUtil.success(HttpStatus.OK));
    }
}
