package com.laurentiu.price_comparator_market.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ProcessedFile {
    @Id
    private String fileName;
    private LocalDateTime processedAt;
    private String fileType;
}