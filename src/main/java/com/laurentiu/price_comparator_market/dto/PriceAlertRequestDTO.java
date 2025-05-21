package com.laurentiu.price_comparator_market.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) used for creating a price alert.
 * Represents the information required to set up a notification when a product's price
 * falls below or reaches a specified target value.
 *
 * @param productName  The name of the product for which the price alert is being created. (required)
 * @param productBrand The brand of the product for which the price alert is being created. (required)
 * @param targetPrice  The target price at which the alert should be triggered. (required)
 * @param userEmail    The email address of the user to notify when the price alert is triggered. (required)
 */
public record PriceAlertRequestDTO(
        String productName,
        String productBrand,
        BigDecimal targetPrice,
        String userEmail
) {
}
