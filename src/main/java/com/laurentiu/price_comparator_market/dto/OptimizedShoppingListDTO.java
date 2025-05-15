package com.laurentiu.price_comparator_market.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO representing an optimized shopping list for a specific supermarket.
 * Contains the calculated best-value distribution of products from the initial shopping list
 * along with total cost and supermarket details.
 *
 * @param supermarketId    Unique identifier of the supermarket
 * @param supermarketName  Name of the supermarket where these items can be purchased
 * @param list            List of products with their optimized prices and quantities
 * @param totalCost       Total calculated cost for all items in this supermarket
 *
*/
public record OptimizedShoppingListDTO(
        Long supermarketId,
        String supermarketName,
        List<ShoppingListItemDTO> list,
        BigDecimal totalCost
) {
}
