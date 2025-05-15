package com.laurentiu.price_comparator_market.service;

import com.laurentiu.price_comparator_market.dto.OptimizedShoppingListDTO;
import com.laurentiu.price_comparator_market.dto.ShoppingItemDTO;
import com.laurentiu.price_comparator_market.dto.ShoppingListItemDTO;
import com.laurentiu.price_comparator_market.entity.Discount;
import com.laurentiu.price_comparator_market.entity.Price;
import com.laurentiu.price_comparator_market.entity.Product;
import com.laurentiu.price_comparator_market.entity.Supermarket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsible for optimizing shopping lists to find the best prices across supermarkets.
 *
 * This service:
 * - Takes a simple shopping list with product names and quantities
 * - Finds the lowest prices for each product considering active discounts
 * - Groups products by supermarket for optimal shopping experience
 * - Calculates total costs for each supermarket group
 * - Returns organized shopping lists per supermarket with detailed pricing
 */

@Service
@RequiredArgsConstructor
public class ShoppingBasketService {
    private final PriceService priceService;
    private final DiscountService discountService;

    public List<OptimizedShoppingListDTO> optimizeShoppingBasket(List<ShoppingItemDTO> shoppingList) {
        LocalDate today = LocalDate.now();
        Map<Supermarket, List<ShoppingListItemDTO>> optimizedList = new HashMap<>();

        for (ShoppingItemDTO item : shoppingList) {
            Price bestPrice = priceService.findLowestPriceForProduct(item.productName(), today);
            if (bestPrice == null) continue;

            Optional<Discount> discount = discountService.getActiveDiscountsForProduct(item.productName()).stream()
                    .max(Comparator.comparing(Discount::getPercentage));

            BigDecimal finalUnitPrice = priceService.calculateFinalPrice(bestPrice, discount);
            BigDecimal totalPrice = finalUnitPrice.multiply(BigDecimal.valueOf(item.quantity()));

            Product product = bestPrice.getProduct();

            ShoppingListItemDTO shoppingListItem = new ShoppingListItemDTO(
                    product.getId(),
                    product.getName(),
                    item.quantity(),
                    finalUnitPrice,
                    totalPrice
            );

            Supermarket supermarket = bestPrice.getSupermarket();
            optimizedList.computeIfAbsent(supermarket, k -> new ArrayList<>()).add(shoppingListItem);

        }

            return optimizedList.entrySet().stream()
                    .map(entry -> new OptimizedShoppingListDTO(
                            entry.getKey().getId(),
                            entry.getKey().getName(),
                            entry.getValue(),
                            entry.getValue().stream()
                                    .map(ShoppingListItemDTO::totalPrice)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    ))
                    .collect(Collectors.toList());

    }
}
