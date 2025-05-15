package com.laurentiu.price_comparator_market.controller;

import com.laurentiu.price_comparator_market.dto.OptimizedShoppingListDTO;
import com.laurentiu.price_comparator_market.dto.ShoppingItemDTO;
import com.laurentiu.price_comparator_market.service.ShoppingBasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-basket")
@RequiredArgsConstructor
public class ShoppingBasketController {
    private final ShoppingBasketService shoppingBasketService;

    @PostMapping("/optimize")
    public ResponseEntity<List<OptimizedShoppingListDTO>> optimizeShoppingBasket(
            @RequestBody List<ShoppingItemDTO> shoppingList) {
        return ResponseEntity.ok(shoppingBasketService.optimizeShoppingBasket(shoppingList));

    }
}
