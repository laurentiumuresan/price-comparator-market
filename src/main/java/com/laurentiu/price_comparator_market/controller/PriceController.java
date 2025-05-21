package com.laurentiu.price_comparator_market.controller;

import com.laurentiu.price_comparator_market.dto.PriceHistoryRequestDTO;
import com.laurentiu.price_comparator_market.dto.PricePerUnitDTO;
import com.laurentiu.price_comparator_market.dto.ProductPriceHistoryDTO;
import com.laurentiu.price_comparator_market.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{productName}/per-unit")
    public ResponseEntity<List<PricePerUnitDTO>> getPricePerUnit(@PathVariable String productName){
        return ResponseEntity.ok(priceService.getPricePerUnit(productName));
    }
}
