package com.laurentiu.price_comparator_market.repository;

import com.laurentiu.price_comparator_market.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PriceRepository extends JpaRepository<Price, Long> {
}