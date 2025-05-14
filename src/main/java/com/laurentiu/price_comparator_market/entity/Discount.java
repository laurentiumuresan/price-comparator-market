package com.laurentiu.price_comparator_market.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;


@Entity
@Data
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Supermarket supermarket;

    private float percentage;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDate timestamp;
}
