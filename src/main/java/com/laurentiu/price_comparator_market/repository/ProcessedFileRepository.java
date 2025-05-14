package com.laurentiu.price_comparator_market.repository;

import com.laurentiu.price_comparator_market.entity.ProcessedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedFileRepository extends JpaRepository<ProcessedFile, String> {
}