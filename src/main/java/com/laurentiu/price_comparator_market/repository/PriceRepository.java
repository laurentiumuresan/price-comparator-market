package com.laurentiu.price_comparator_market.repository;

import com.laurentiu.price_comparator_market.entity.Price;
import com.laurentiu.price_comparator_market.entity.Product;
import com.laurentiu.price_comparator_market.entity.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Long> {
    Optional<Price> findBySupermarketAndProductAndTimestamp(Supermarket supermarket, Product product, LocalDateTime discountDateTime);
}