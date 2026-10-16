package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.Promo;
import com.api.v1.bearerauth.model.request.PromoCreateRequest;
import com.api.v1.bearerauth.model.request.PromoValidateRequest;
import com.api.v1.bearerauth.model.response.PromoDetailsResponse;
import com.api.v1.bearerauth.model.response.PromoResponse;
import com.api.v1.bearerauth.repository.PromoRepository;
import com.api.v1.bearerauth.service.PromoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromoServiceImpl implements PromoService {

    private final PromoRepository promoRepository;

    @Override
    public PromoResponse validate(PromoValidateRequest request) {
        String code = request.getCode() == null ? "" : request.getCode().trim().toUpperCase();

        double subtotal = request.getSubtotal();

        Optional<Promo> result = promoRepository.findByCodeIgnoreCase(code);

        if (result.isEmpty()) {
            return invalidResponse(code);
        }

        Promo promo = result.get();
        LocalDateTime now = LocalDateTime.now();

        boolean started = promo.getStartAt() == null || !now.isBefore(promo.getStartAt());

        boolean notExpired = promo.getEndAt() == null || !now.isAfter(promo.getEndAt());

        boolean valid = Boolean.TRUE.equals(promo.getActive()) && started && notExpired && subtotal >= promo.getMinimumSubtotal() && subtotal > 0;

        double discount = valid ? roundMoney(subtotal * promo.getDiscountRate()) : 0.0;

        String message = valid ? "Discount applied successfully." : "Promo code is inactive, expired, or does not meet the minimum subtotal.";

        return new PromoResponse(valid, valid ? promo.getCode() : code, discount, promo.getDiscountRate(), message);
    }

    @Override
    public List<PromoDetailsResponse> findActive() {
        return promoRepository.findByActiveTrue().stream().filter(this::isCurrentlyAvailable).map(this::toResponse).toList();
    }

    @Override
    public List<PromoDetailsResponse> findAll() {
        return promoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public PromoDetailsResponse getById(Long id) {
        return toResponse(findPromo(id));
    }

    @Override
    @Transactional
    public PromoDetailsResponse create(PromoCreateRequest request) {
        validateRequest(request);

        String code = normalizeCode(request.getCode());

        if (promoRepository.existsByCodeIgnoreCase(code)) {
            throw new CustomException(HttpStatus.CONFLICT, "Promo code already exists");
        }

        Promo promo = new Promo();
        applyRequest(promo, request);
        promo.setActive(true);

        return toResponse(promoRepository.save(promo));
    }

    @Override
    @Transactional
    public PromoDetailsResponse update(Long id, PromoCreateRequest request) {

        validateRequest(request);

        Promo promo = findPromo(id);
        String code = normalizeCode(request.getCode());

        if (promoRepository.existsByCodeIgnoreCaseAndPromoIdNot(code, id)) {
            throw new CustomException(HttpStatus.CONFLICT, "Promo code already exists");
        }

        applyRequest(promo, request);

        return toResponse(promoRepository.save(promo));
    }

    @Override
    @Transactional
    public PromoDetailsResponse activate(Long id) {
        Promo promo = findPromo(id);
        promo.setActive(true);

        return toResponse(promoRepository.save(promo));
    }

    @Override
    @Transactional
    public PromoDetailsResponse deactivate(Long id) {
        Promo promo = findPromo(id);
        promo.setActive(false);

        return toResponse(promoRepository.save(promo));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Promo promo = findPromo(id);
        promoRepository.delete(promo);
    }

    private Promo findPromo(Long id) {
        return promoRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Promo not found"));
    }

    private void validateRequest(PromoCreateRequest request) {
        if (request.getStartAt() != null && request.getEndAt() != null && request.getEndAt().isBefore(request.getStartAt())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "End time cannot be before start time");
        }

        if (request.getDiscountRate() == null || !Double.isFinite(request.getDiscountRate()) || request.getDiscountRate() <= 0 || request.getDiscountRate() > 1) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Discount rate must be greater than 0 and at most 1");
        }

        if (request.getMinimumSubtotal() == null || !Double.isFinite(request.getMinimumSubtotal()) || request.getMinimumSubtotal() < 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Minimum subtotal must be zero or greater");
        }
    }

    private void applyRequest(Promo promo, PromoCreateRequest request) {

        promo.setCode(normalizeCode(request.getCode()));
        promo.setDescription(request.getDescription());
        promo.setDiscountRate(request.getDiscountRate());
        promo.setMinimumSubtotal(request.getMinimumSubtotal());
        promo.setStartAt(request.getStartAt());
        promo.setEndAt(request.getEndAt());
    }

    private boolean isCurrentlyAvailable(Promo promo) {
        LocalDateTime now = LocalDateTime.now();

        return (promo.getStartAt() == null || !now.isBefore(promo.getStartAt())) && (promo.getEndAt() == null || !now.isAfter(promo.getEndAt()));
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }

    private PromoDetailsResponse toResponse(Promo promo) {
        return new PromoDetailsResponse(promo.getPromoId(), promo.getCode(), promo.getDescription(), promo.getDiscountRate(), promo.getMinimumSubtotal(), promo.getStartAt(), promo.getEndAt(), promo.getActive());
    }

    private PromoResponse invalidResponse(String code) {
        return new PromoResponse(false, code, 0.0, 0.0, "Promo code does not exist.");
    }

    private double roundMoney(double amount) {
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}

