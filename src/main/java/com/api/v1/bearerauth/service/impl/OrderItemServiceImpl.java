package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.*;
import com.api.v1.bearerauth.model.enumerate.OptionType;
import com.api.v1.bearerauth.model.request.OrderItemRequest;
import com.api.v1.bearerauth.repository.ProductOptionRepository;
import com.api.v1.bearerauth.repository.ProductRepository;
import com.api.v1.bearerauth.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    @Override
    public List<ProductOrder> createItems(OrderTb order, List<OrderItemRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Order items cannot be empty");
        }

        List<ProductOrder> items = new ArrayList<>();

        for (OrderItemRequest request : requests) {
            if (request.getQuantity() == null || request.getQuantity() < 1) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Quantity must be at least 1");
            }

            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Product not found"));

            List<Long> ids = request.getOptionIds() == null ? List.of() : request.getOptionIds();
            if (new HashSet<>(ids).size() != ids.size()) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Duplicate option IDs are not allowed");
            }

            List<ProductOption> availableOptions = productOptionRepository
                    .findByProduct_ProductIdAndActiveTrue(product.getProductId());
            Map<Long, ProductOption> availableById = availableOptions.stream()
                    .collect(Collectors.toMap(ProductOption::getOptionId, Function.identity()));

            List<ProductOption> selected = new ArrayList<>();
            for (Long id : ids) {
                ProductOption option = availableById.get(id);
                if (option == null) {
                    throw new CustomException(HttpStatus.BAD_REQUEST,
                            "Option " + id + " is unavailable for this product");
                }
                selected.add(option);
            }

            long sizeCount = selected.stream().filter(o -> o.getOptionType() == OptionType.SIZE).count();
            boolean productHasSizes = availableOptions.stream().anyMatch(o -> o.getOptionType() == OptionType.SIZE);
            if (sizeCount > 1 || (productHasSizes && sizeCount != 1)) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Select exactly one size for this product");
            }

            ProductOption selectedSize = selected.stream()
                    .filter(o -> o.getOptionType() == OptionType.SIZE)
                    .findFirst().orElse(null);

            // SIZE price is the full price for that size, not an amount added to product.unitPrice.
            double effectiveUnitPrice = product.getUnitPrice();
            if (selectedSize != null) {
                if (selectedSize.getPrice() != null) {
                    effectiveUnitPrice = selectedSize.getPrice();
                } else {
                    // Backward compatibility for older size options created before the dedicated price field.
                    effectiveUnitPrice += selectedSize.getExtraPrice() == null ? 0.0 : selectedSize.getExtraPrice();
                }
            }

            ProductOrder item = new ProductOrder();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            item.setUnitPrice(effectiveUnitPrice);

            List<ProductOrderOption> snapshots = new ArrayList<>();
            for (ProductOption option : selected) {
                ProductOrderOption snapshot = new ProductOrderOption();
                snapshot.setOptionName(option.getOptionName());
                snapshot.setOptionType(option.getOptionType());
                snapshot.setPrice(option.getPrice());
                // Add-on charges are additive. A SIZE option's amount is already in item.unitPrice.
                snapshot.setExtraPrice(option.getOptionType() == OptionType.ADD_ON
                        ? option.getExtraPrice() : 0.0);
                snapshot.setProductOrder(item);
                snapshots.add(snapshot);
            }

            item.setSelectedOptions(snapshots);
            items.add(item);
        }

        return items;
    }
}
