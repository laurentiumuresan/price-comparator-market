package com.laurentiu.price_comparator_market.repository;

import com.laurentiu.price_comparator_market.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DiscountRepository extends JpaRepository <Discount, Long> {

    List<Discount> findByTimestampAfter(LocalDate date);

    @Query ("SELECT d FROM Discount d ORDER BY d.timestamp desc, d.percentage desc")
    List<Discount> findAllOrderByTimestampDescPercentageDesc();
}

