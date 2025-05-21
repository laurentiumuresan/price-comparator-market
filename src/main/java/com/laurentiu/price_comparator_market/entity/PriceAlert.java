package com.laurentiu.price_comparator_market.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private BigDecimal targetPrice;
    private boolean isActive;
    private LocalDateTime createdAt;
    private String userEmail;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
