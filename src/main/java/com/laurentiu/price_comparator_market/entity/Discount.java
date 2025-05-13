package com.laurentiu.price_comparator_market.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "price_id")
    private Price price;

    private BigDecimal discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
