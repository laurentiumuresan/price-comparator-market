package com.laurentiu.price_comparator_market.controller;

import com.laurentiu.price_comparator_market.dto.PriceAlertRequestDTO;
import com.laurentiu.price_comparator_market.entity.PriceAlert;
import com.laurentiu.price_comparator_market.service.PriceAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/price-alerts")
@RequiredArgsConstructor
public class PriceAlertController {
    private final PriceAlertService priceAlertService;

    @PostMapping("/create")
    public ResponseEntity<PriceAlert> createPriceAlert(@RequestBody PriceAlertRequestDTO request){
        return ResponseEntity.ok(priceAlertService.createPriceAlert(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriceAlert(@PathVariable Long id){
        priceAlertService.deletePriceAlert(id);
        return ResponseEntity.ok().build();
    }

}
