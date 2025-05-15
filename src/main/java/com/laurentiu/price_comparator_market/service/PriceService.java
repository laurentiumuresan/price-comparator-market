package com.laurentiu.price_comparator_market.service;

import com.laurentiu.price_comparator_market.entity.Discount;
import com.laurentiu.price_comparator_market.entity.Price;
import com.laurentiu.price_comparator_market.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceService {
    public final PriceRepository priceRepository;

    public BigDecimal calculateFinalPrice(Price price, Optional<Discount> discount){
        if (discount.isPresent()) {
            float discountMultiplier = 1 - (discount.get().getPercentage() / 100f);
            return price.getAmount().multiply(BigDecimal.valueOf(discountMultiplier));
        }
        return price.getAmount();
    }

    public Price findLowestPriceForProduct(String productName, LocalDate date){
        List<Price> prices = priceRepository.findLatestPriceByProductId(productName);
        return prices.stream()
                .min(Comparator.comparing(Price::getAmount))
                .orElse(null);
    }
}