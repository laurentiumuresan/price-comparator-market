package com.laurentiu.price_comparator_market.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO representing a single price point for a product at a specific date.
 * Contains detailed pricing information including original price, any applicable discounts,
 * and product identification details.
 */

public record PricePointDTO(
        LocalDate date,
        BigDecimal originalPrice,
        BigDecimal discountedPrice,
        Float discountPercentage,
        String supermarketName,
        String brand,
        String category,
        String currency
) {
}
