package com.laurentiu.price_comparator_market.controller;

import com.laurentiu.price_comparator_market.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/discounts")
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;

    @GetMapping("/new")
    public ResponseEntity<List<Map<String, Object>>> getNewDiscounts() {
        return ResponseEntity.ok(discountService.getNewDiscounts());
    }

    @GetMapping("/best")
    public ResponseEntity<List<Map<String, Object>>> getBestDiscountedProducts(){
        return ResponseEntity.ok(discountService.getBestDiscountedProducts());
    }
}
