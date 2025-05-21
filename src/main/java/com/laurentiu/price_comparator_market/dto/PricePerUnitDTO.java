package com.laurentiu.price_comparator_market.dto;

import java.math.BigDecimal;

public record PricePerUnitDTO(
        String supermarket,
        BigDecimal price,
        BigDecimal pricePerUnit,
        String unit

) {
}
