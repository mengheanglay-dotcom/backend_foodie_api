package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.*;
import com.api.v1.bearerauth.model.enumerate.*;
import com.api.v1.bearerauth.model.request.*;
import com.api.v1.bearerauth.model.response.*;
import com.api.v1.bearerauth.repository.*;
import com.api.v1.bearerauth.security.CurrentUserService;
import com.api.v1.bearerauth.service.OrderItemService;
import com.api.v1.bearerauth.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final double DELIVERY_FEE = 2.0;
    private static final double PROMO_RATE = 0.10;

    private final OrderTbRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CurrentUserService currentUserService;
    private final OrderItemService orderItemService;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> list(int page, int size, Sort.Direction direction) {

        AppUser user = currentUserService.getCurrentUser();

        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(direction, "orderId"));

        if (user.getRole().name().equals("ADMIN")) {
            return orderRepository.findAll(pageable).map(this::toResponse);
        }

        return orderRepository.findByCustomerUser(user, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse get(Long id) {
        OrderTb order = findOrder(id);
        checkOwnerOrAdmin(order);

        return toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse create(OrderRequest request) {
        return saveNew(request);
    }

    @Override
    @Transactional
    public OrderResponse update(Long id, OrderRequest request) {
        OrderTb order = findOrder(id);

        checkOwnerOrAdmin(order);

        if (order.getStatus() != OrderStatus.processing) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Only processing orders can be updated");
        }

        applyOrder(order, request);

        return toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        OrderTb order = findOrder(id);

        checkOwnerOrAdmin(order);

        if (order.getStatus() != OrderStatus.processing) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Only processing orders can be deleted");
        }

        orderRepository.delete(order);
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatusRequest request) {

        OrderTb order = findOrder(id);

        order.setStatus(request.getStatus());

        return toResponse(orderRepository.save(order));
    }

    private OrderResponse saveNew(OrderRequest request) {
        AppUser user = currentUserService.getCurrentUser();

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Customer profile not found"));

        OrderTb order = new OrderTb();

        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.processing);

        applyOrder(order, request);

        return toResponse(orderRepository.save(order));
    }


    private void applyOrder(OrderTb order, OrderRequest request) {

        if (request.getOrderType() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Order type is required");
        }

        if (request.getPaymentMethod() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Payment method is required");
        }

        order.setOrderType(request.getOrderType());
        order.setPaymentMethod(request.getPaymentMethod());

        // Set delivery address and fee first
        boolean isDelivery = request.getOrderType() == OrderType.DELIVERY;

        if (isDelivery && (request.getDeliveryAddress() == null || request.getDeliveryAddress().isBlank())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Delivery address is required");
        }

        order.setDeliveryAddress(isDelivery ? request.getDeliveryAddress().trim() : "");

        order.setDeliveryFee(isDelivery ? DELIVERY_FEE : 0.0);

        // Rebuild order items
        order.getItems().clear();

        List<ProductOrder> items = orderItemService.createItems(order, request.getItems());

        order.getItems().addAll(items);

        // Calculate subtotal including size and add-on prices
        double subtotal = items.stream().mapToDouble(item -> {
            double optionsTotal = item.getSelectedOptions().stream().mapToDouble(option -> option.getExtraPrice() == null ? 0.0 : option.getExtraPrice()).sum();

            return (item.getUnitPrice() + optionsTotal) * item.getQuantity();
        }).sum();

        subtotal = roundMoney(subtotal);

        // Apply promo code
        String code = request.getPromoCode() == null ? "" : request.getPromoCode().trim().toUpperCase();

        double discount = "FOODIE10".equals(code) && subtotal > 0 ? roundMoney(subtotal * PROMO_RATE) : 0.0;

        order.setPromoCode(discount > 0 ? code : "");
        order.setDiscount(discount);

        // Calculate final total
        double total = subtotal + order.getDeliveryFee() - discount;

        order.setTotalAmount(roundMoney(total));
    }


    private OrderTb findOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    private void checkOwnerOrAdmin(OrderTb order) {
        AppUser user = currentUserService.getCurrentUser();

        if (!user.getRole().name().equals("ADMIN") && !order.getCustomer().getUser().getId().equals(user.getId())) {

            throw new CustomException(HttpStatus.FORBIDDEN, "You can only access your own orders");
        }
    }


    private OrderResponse toResponse(OrderTb order) {

        OrderResponse response = new OrderResponse();

        response.setOrderId(order.getOrderId());
        response.setOrderDate(order.getOrderDate());
        response.setStatus(order.getStatus());
        response.setOrderType(order.getOrderType());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setDeliveryAddress(order.getDeliveryAddress());

        response.setDeliveryFee(roundMoney(order.getDeliveryFee() == null ? 0.0 : order.getDeliveryFee()));

        response.setTotalAmount(roundMoney(order.getTotalAmount() == null ? 0.0 : order.getTotalAmount()));

        response.setPromoCode(order.getPromoCode() == null ? "" : order.getPromoCode());

        response.setDiscount(roundMoney(order.getDiscount() == null ? 0.0 : order.getDiscount()));

        response.setCustomerId(order.getCustomer().getCustomerId());

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        int totalItems = 0;

        for (ProductOrder item : order.getItems()) {

            OrderItemResponse itemResponse = new OrderItemResponse();

            itemResponse.setId(item.getId());
            itemResponse.setProductId(item.getProduct().getProductId());
            itemResponse.setName(item.getProduct().getProductName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setUnitPrice(roundMoney(item.getUnitPrice()));

            // Get the selected size
            String size = item.getSelectedOptions().stream().filter(option -> option.getOptionType() == OptionType.SIZE).map(ProductOrderOption::getOptionName).findFirst().orElse(null);

            itemResponse.setSize(size);

            // Get selected add-on names
            List<String> addOns = item.getSelectedOptions().stream().filter(option -> option.getOptionType() == OptionType.ADD_ON).map(ProductOrderOption::getOptionName).toList();

            itemResponse.setAddOns(addOns);

            // Base price + selected option prices, multiplied by quantity
            double optionsTotal = item.getSelectedOptions().stream().mapToDouble(option -> option.getExtraPrice() == null ? 0.0 : option.getExtraPrice()).sum();

            double itemSubtotal = (item.getUnitPrice() + optionsTotal) * item.getQuantity();

            itemResponse.setSubtotal(roundMoney(itemSubtotal));

            itemResponses.add(itemResponse);
            totalItems += item.getQuantity();
        }

        response.setItems(itemResponses);
        response.setTotalItems(totalItems);

        return response;
    }

    private double roundMoney(double amount) {
        return java.math.BigDecimal.valueOf(amount)
                .setScale(2, java.math.RoundingMode.HALF_UP)
                .doubleValue();
    }


}

