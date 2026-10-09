package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.model.request.PromoValidateRequest;
import com.api.v1.bearerauth.model.response.PromoResponse;
import com.api.v1.bearerauth.service.PromoService;
import org.springframework.stereotype.Service;

@Service
public class PromoServiceImpl implements PromoService {
    private static final String CODE = "FOODIE10";
    private static final double RATE = 0.10;
    @Override public PromoResponse validate(PromoValidateRequest request) {
        String code=request.getCode().trim().toUpperCase();
        double subtotal=request.getSubtotal();
        boolean valid=CODE.equals(code) && subtotal>0;
        double discount=valid ? subtotal*RATE : 0;
        String message=valid ? "10% discount applied to your food subtotal." : "That promo code is not valid.";
        return new PromoResponse(valid, valid?CODE:code, discount, RATE, message);
    }
}
