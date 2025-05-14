package com.laurentiu.price_comparator_market.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JsonIgnore
    private Supermarket supermarket;

    private float percentage;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDate timestamp;
}
