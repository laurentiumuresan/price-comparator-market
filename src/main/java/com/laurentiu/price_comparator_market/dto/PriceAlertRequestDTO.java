package com.laurentiu.price_comparator_market.dto;

import java.math.BigDecimal;

public record PriceAlertRequestDTO(
        String productName,
        BigDecimal targetPrice,
        String userEmail
) {
}
