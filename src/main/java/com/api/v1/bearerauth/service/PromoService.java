package com.api.v1.bearerauth.service;

import com.api.v1.bearerauth.model.request.PromoValidateRequest;
import com.api.v1.bearerauth.model.response.PromoResponse;

public interface PromoService { PromoResponse validate(PromoValidateRequest request); }
