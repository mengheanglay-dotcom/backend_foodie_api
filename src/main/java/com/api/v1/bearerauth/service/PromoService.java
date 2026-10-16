package com.api.v1.bearerauth.service;

import com.api.v1.bearerauth.model.request.PromoCreateRequest;
import com.api.v1.bearerauth.model.request.PromoValidateRequest;
import com.api.v1.bearerauth.model.response.PromoDetailsResponse;
import com.api.v1.bearerauth.model.response.PromoResponse;

import java.util.List;


public interface PromoService {

    PromoResponse validate(PromoValidateRequest request);

    List<PromoDetailsResponse> findActive();

    List<PromoDetailsResponse> findAll();

    PromoDetailsResponse getById(Long id);

    PromoDetailsResponse create(PromoCreateRequest request);

    PromoDetailsResponse update(Long id, PromoCreateRequest request);

    PromoDetailsResponse activate(Long id);

    PromoDetailsResponse deactivate(Long id);

    void delete(Long id);
}
