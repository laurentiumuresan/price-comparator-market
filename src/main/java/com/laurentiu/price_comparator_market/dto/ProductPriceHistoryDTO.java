package com.laurentiu.price_comparator_market.dto;

import java.util.List;

/**
 * DTO representing the complete price history for a specific product.
 * Encapsulates the product name and a chronological list of price points,
 * showing how the product's price has changed over time.
 */

public record ProductPriceHistoryDTO(
        String productName,
        List<PricePointDTO> priceHistory
) {
}
