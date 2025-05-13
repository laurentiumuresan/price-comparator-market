package com.laurentiu.price_comparator_market.entity;

import com.laurentiu.price_comparator_market.entity.Discount;
import com.laurentiu.price_comparator_market.entity.Product;
import com.laurentiu.price_comparator_market.entity.Supermarket;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Supermarket Supermarket;
    
    private BigDecimal amount;
    private LocalDateTime timestamp;
    
    @OneToMany(mappedBy = "price")
    private List<Discount> discounts;
}