package com.laurentiu.price_comparator_market.dto;

import java.math.BigDecimal;

/**
 * DTO representing a product item after shopping list optimization.
 * Contains detailed pricing for each product allocated to a specific supermarket
 * including individual unit prices and calculated total cost for requested quantity.
 *
 * @param productId    Unique identifier of the product
 * @param productName  Name of the product from supermarket's inventory
 * @param quantity     Requested quantity of the product
 * @param unitPrice    Best available price per unit (including any active discounts)
 * @param totalPrice   Total calculated price for the requested quantity
 */
public record ShoppingListItemDTO(
        String productId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
}