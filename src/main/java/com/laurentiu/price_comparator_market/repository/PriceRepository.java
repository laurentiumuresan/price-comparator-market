package com.laurentiu.price_comparator_market.repository;

import com.laurentiu.price_comparator_market.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface PriceRepository extends JpaRepository<Price, Long> {

    @Query("select p from Price p join p.product prod where prod.name = :productName" +
            " and p.timestamp= (select max(p2.timestamp) " +
            "from Price p2 join p2.product prod2 where prod2.name =: productName)")
    List<Price> findLatestPriceByProductId(@Param("productName") String productName);


    @Query("select p from Price p join fetch p.product prod join fetch p.supermarket s " +
            "where lower(prod.name) like lower(concat('%', :productName, '%') )" +
            "and p.timestamp between :startDate and :endDate and " +
            "(:supermarketName is null or lower(s.name) like lower(concat('%', :supermarketName, '%') ) ) " +
            "and (:brand is null or lower(prod.brand) = lower(:brand) ) and " +
            "(:category is null or lower(prod.category) = lower(:category) )order by p.timestamp desc , s.name asc")
    List<Price> findPriceHistoryForProduct(
            @Param("productName") String productName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("supermarketName") String supermarketName,
            @Param("brand") String brand,
            @Param("category") String category
    );
}
