package com.laurentiu.price_comparator_market.controller;

import com.laurentiu.price_comparator_market.dto.PriceHistoryRequestDTO;
import com.laurentiu.price_comparator_market.dto.ProductPriceHistoryDTO;
import com.laurentiu.price_comparator_market.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;

    @PostMapping("/history")
    public ResponseEntity<ProductPriceHistoryDTO> getProductPriceHistory(
            @RequestBody PriceHistoryRequestDTO request){
        return ResponseEntity.ok(priceService.getProductPriceHistory(request));
    }
}
