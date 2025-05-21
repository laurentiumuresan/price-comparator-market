package com.laurentiu.price_comparator_market.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a price point for a specific product.
 * Includes detailed pricing, discount, and categorization data at a specific point in time
 * and location (supermarket). Useful for tracking price history or comparing pricing details
 * across different supermarkets and product categories.
 *
 * @param date              The date corresponding to this price point.
 * @param originalPrice     The original price of the product before any discounts were applied.
 * @param discountedPrice   The price of the product after applying discounts, if applicable.
 * @param discountPercentage The percentage discount applied to the product.
 * @param supermarketName   The name of the supermarket where the product is sold.
 * @param brand             The brand name of the product.
 * @param category          The category to which the product belongs.
 * @param currency          The currency in which the price values are expressed.
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
