package com.laurentiu.price_comparator_market.dto;

/**
 * DTO for requesting product price history information.
 * Contains filtering criteria to narrow down the search results based on various parameters
 * such as time period, supermarket, brand, and category.
 *
 * @param productName      Name of the product to search for (required)
 * @param lastNDays       Number of days to look back in the price history
 * @param supermarketName Filter results by specific supermarket name (optional)
 * @param brand          Filter results by specific product brand (optional)
 * @param category       Filter results by product category (optional)
 */

public record PriceHistoryRequestDTO(
        String productName,
        Integer lastNDays,
        String supermarketName,
        String brand,
        String category
) {
}
