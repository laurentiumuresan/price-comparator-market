package com.laurentiu.price_comparator_market.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing the price history of a specific product.
 * Encapsulates the product's name and a list of price points. Each price point contains
 * detailed pricing information, including any applied discounts, the supermarket where
 * the price was observed, and additional metadata such as date and currency.
 *
 * This DTO is primarily used for retrieving and transferring historical pricing data
 * for a product across a specified time frame or under given filtering criteria.
 *
 * @param productName  The name of the product whose price history is being represented.
 * @param priceHistory A list of {@code PricePointDTO} objects containing individual
 *                     price points for the product. Each price point represents a snapshot
 *                     of the product's pricing and metadata at a given time and location.
 */

public record ProductPriceHistoryDTO(
        String productName,
        List<PricePointDTO> priceHistory
) {
}
