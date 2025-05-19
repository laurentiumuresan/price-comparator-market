package com.laurentiu.price_comparator_market.service;

import com.laurentiu.price_comparator_market.dto.PriceHistoryRequestDTO;
import com.laurentiu.price_comparator_market.dto.PricePointDTO;
import com.laurentiu.price_comparator_market.dto.ProductPriceHistoryDTO;
import com.laurentiu.price_comparator_market.entity.Discount;
import com.laurentiu.price_comparator_market.entity.Price;
import com.laurentiu.price_comparator_market.entity.Product;
import com.laurentiu.price_comparator_market.repository.DiscountRepository;
import com.laurentiu.price_comparator_market.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceService {
    public final PriceRepository priceRepository;
    private final DiscountRepository discountRepository;

    public BigDecimal calculateFinalPrice(Price price, Optional<Discount> discount) {
        if (discount.isPresent()) {
            float discountMultiplier = 1 - (discount.get().getPercentage() / 100f);
            return price.getAmount().multiply(BigDecimal.valueOf(discountMultiplier));
        }
        return price.getAmount();
    }

    public Price findLowestPriceForProduct(String productName, LocalDate date) {
        List<Price> prices = priceRepository.findLatestPriceByProductId(productName);
        return prices.stream()
                .min(Comparator.comparing(Price::getAmount))
                .orElse(null);
    }

    public ProductPriceHistoryDTO getProductPriceHistory(PriceHistoryRequestDTO request) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(request.lastNDays());

        List<Price> prices = priceRepository.findPriceHistoryForProduct(
                request.productName(),
                startDate,
                endDate,
                request.supermarketName(),
                request.brand(),
                request.category()
        );

        if (prices.isEmpty()) return null;

        Price firstPrice = prices.get(0);
        Product product = firstPrice.getProduct();

        List<PricePointDTO> pricePoints = prices.stream().map(price -> {
                    List<Discount> activeDiscounts = discountRepository
                            .findActiveDiscountForProduct(
                                    price.getProduct().getId(),
                                    price.getTimestamp()
                            );

                    Optional<Discount> maxDiscount = activeDiscounts.stream()
                            .max(Comparator.comparing(Discount::getPercentage));

                    BigDecimal discountedPrice = calculateFinalPrice(price, maxDiscount)
                            .setScale(2, RoundingMode.HALF_UP);

                    return new PricePointDTO(
                            price.getTimestamp(),
                            price.getAmount(),
                            discountedPrice,
                            maxDiscount.map(Discount::getPercentage).orElse(0f),
                            price.getSupermarket().getName(),
                            price.getProduct().getBrand(),
                            price.getProduct().getCategory(),
                            price.getCurrency()

                    );
                })
                .toList();
        return new ProductPriceHistoryDTO(
                product.getName(),
                pricePoints
        );
    }
}