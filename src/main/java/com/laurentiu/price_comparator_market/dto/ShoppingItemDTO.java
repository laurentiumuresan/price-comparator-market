package com.laurentiu.price_comparator_market.dto;

/**
 * DTO representing an item in the initial shopping list provided by the user.
 * This is the input format for the shopping basket optimization process.
 *
 * @param productName  Name of the product to be purchased
 * @param quantity    Desired quantity of the product
 */

public record ShoppingItemDTO(String productName, int quantity) {
}