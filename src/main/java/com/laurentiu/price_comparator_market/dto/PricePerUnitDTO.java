package com.laurentiu.price_comparator_market.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing the price details of a product at a specific supermarket,
 * including the price per unit. Useful for comparing the cost-effectiveness of products sold in
 * different packaging units or across various supermarkets.
 *
 * @param supermarket  The name of the supermarket offering the product.
 * @param price        The total price of the product as listed in the supermarket.
 * @param pricePerUnit The calculated price of the product per standard unit (e.g., per kg, per liter).
 * @param unit         The standard unit of measurement (e.g., kg, l) used for the pricePerUnit field.
 */
public record PricePerUnitDTO(
        String supermarket,
        BigDecimal price,
        BigDecimal pricePerUnit,
        String unit

) {
}
