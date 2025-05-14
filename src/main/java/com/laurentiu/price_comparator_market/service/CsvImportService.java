package com.laurentiu.price_comparator_market.service;

/**
 * Service responsible for importing and processing product price and discount data from CSV files.
 *
 * This service handles two types of CSV files:
 * 1. Price files: Contains product prices from various supermarkets
 * 2. Discount files: Contains promotional discounts for products
 *
 * Key features:
 * - Processes all CSV files in a specified directory
 * - Maintains processing history to avoid duplicate imports
 * - Supports transaction management for data consistency
 * - Implements caching for supermarket entities to improve performance
 * - Provides robust error handling and logging
 */

import com.laurentiu.price_comparator_market.entity.*;
import com.laurentiu.price_comparator_market.repository.*;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvImportService {
    private final ProductRepository productRepository;
    private final SupermarketRepository supermarketRepository;
    private final PriceRepository priceRepository;
    private final DiscountRepository discountRepository;
    private final ProcessedFileRepository processedFileRepository;
    private final Map<String, Supermarket> supermarketCache = new HashMap<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Transactional
    public void processDirectory(String directoryPath) {
        try {
            Path mainDirectory = Paths.get(directoryPath);

            Files.list(mainDirectory)
                    .filter(path -> path.toString().toLowerCase().endsWith(".csv"))
                    .forEach(file -> {
                        String fileName = file.getFileName().toString().toLowerCase();

                        if (processedFileRepository.findById(fileName).isPresent()) {
                            log.info("File {} has already been processed. Skipping.", fileName);
                            return;
                        }

                        try {
                            if (fileName.contains("_discounts_")) {
                                log.info("Processing discount file: {}", fileName);
                                importDiscountsFromCsv(file.toString());
                            } else {
                                log.info("Processing price file: {}", fileName);
                                importProductPricesFromCsv(file.toString());
                            }

                            ProcessedFile processedFile = new ProcessedFile();
                            processedFile.setFileName(fileName);
                            processedFile.setProcessedAt(LocalDateTime.now());
                            processedFile.setFileType(fileName.contains("_discounts_") ? "DISCOUNT" : "PRICE");
                            processedFileRepository.save(processedFile);

                            log.info("Successfully processed and marked file: {}", fileName);

                        } catch (Exception e) {
                            log.error("Error processing file {}: {}", fileName, e.getMessage());
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Error accessing directory: " + directoryPath, e);
        }
    }

    private String extractStoreName(String filePath) {
        String fileName = Paths.get(filePath).getFileName().toString();
        return fileName.split("_")[0];
    }

    private LocalDate extractDateTime(String filePath) {
        String fileName = Paths.get(filePath).getFileName().toString();
        String dateStr = fileName.substring(fileName.indexOf("_") + 1).replace(".csv", "");
        if (dateStr.contains("_")) {
            dateStr = dateStr.split("_")[1];
        }
        log.info("Extracted date: {} from file: {}", dateStr, fileName);
        return LocalDate.parse(dateStr);
    }

    private void importProductPricesFromCsv(String filePath) {
        try {
            CSVParser csvParser = new CSVParserBuilder()
                    .withSeparator(';')
                    .build();

            try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                    .withSkipLines(1)
                    .withCSVParser(csvParser)
                    .build()) {

                String storeName = extractStoreName(filePath);
                LocalDate priceDateTime = extractDateTime(filePath);
                Supermarket supermarket = getSupermarket(storeName);

                String[] line;
                while ((line = reader.readNext()) != null) {
                    try {
                        validatePriceLine(line);
                        processProductLine(line, supermarket, priceDateTime);
                    } catch (Exception e) {
                        log.error("Error processing price line: {}", Arrays.toString(line), e);
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error processing the CSV: " + filePath, e);
        }
    }

    private void importDiscountsFromCsv(String filePath) {
        try {
            CSVParser csvParser = new CSVParserBuilder()
                    .withSeparator(';')
                    .build();

            try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                    .withSkipLines(1)
                    .withCSVParser(csvParser)
                    .build()) {

                String storeName = extractStoreName(filePath);
                LocalDate discountDateTime = extractDateTime(filePath);
                Supermarket supermarket = getSupermarket(storeName);

                String[] line;
                while ((line = reader.readNext()) != null) {
                    try {
                        validateDiscountLine(line);
                        processDiscountLine(line, supermarket, discountDateTime);
                    } catch (Exception e) {
                        log.error("Error processing discount line: {}", Arrays.toString(line), e);
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error processing discount CSV: " + filePath, e);
        }
    }

    private void validateDiscountLine(String[] line) {
        if (line.length != 9) {
            throw new IllegalArgumentException("Invalid discount line format. Expected 9 fields, got " + line.length);
        }
    }

    private void validatePriceLine(String[] line) {
        if (line.length != 8) {
            throw new IllegalArgumentException("Invalid price line format. Expected 8 fields, got " + line.length);
        }
    }


    private void processDiscountLine(String[] line, Supermarket supermarket, LocalDate discountDateTime) {
        String productId = line[0];
        String productName = line[1];
        String brand = line[2];
        float packageQuantity = Float.parseFloat(line[3]);
        String packageUnit = line[4];
        String category = line[5];
        LocalDateTime fromDate = LocalDate.parse(line[6], DATE_FORMATTER).atStartOfDay();
        LocalDateTime toDate = LocalDate.parse(line[7], DATE_FORMATTER).atStartOfDay();
        float discountPercentage = Float.parseFloat(line[8]);

        Product product = productRepository.findById(productId)
                .orElseGet(() -> {
                    Product newProduct = new Product();
                    newProduct.setId(productId);
                    newProduct.setName(productName);
                    newProduct.setCategory(category);
                    newProduct.setBrand(brand);
                    newProduct.setPackageQuantity(packageQuantity);
                    newProduct.setPackageUnit(packageUnit);
                    newProduct.setPrices(new ArrayList<>());
                    return productRepository.save(newProduct);
                });

        Discount discount = new Discount();
        discount.setSupermarket(supermarket);
        discount.setProduct(product);
        discount.setPercentage(discountPercentage);
        discount.setTimestamp(discountDateTime);
        discount.setStartDate(fromDate.toLocalDate());
        discount.setEndDate(toDate.toLocalDate());

        discountRepository.save(discount);
        log.info("Processed discount for product {} at store {}", productId, supermarket.getName());
    }

    private void processProductLine(String[] line, Supermarket supermarket, LocalDate priceDateTime) {
        String productId = line[0];
        String productName = line[1];
        String category = line[2];
        String brand = line[3];
        float packageQuantity = Float.parseFloat(line[4]);
        String packageUnit = line[5];
        BigDecimal amount = new BigDecimal(line[6]);
        String currency = line[7];

        Product product = productRepository.findById(productId)
                .orElseGet(() -> {
                    Product newProduct = new Product();
                    newProduct.setId(productId);
                    newProduct.setName(productName);
                    newProduct.setCategory(category);
                    newProduct.setBrand(brand);
                    newProduct.setPackageQuantity(packageQuantity);
                    newProduct.setPackageUnit(packageUnit);
                    newProduct.setPrices(new ArrayList<>());
                    return productRepository.save(newProduct);
                });

        Price price = new Price();
        price.setProduct(product);
        price.setSupermarket(supermarket);
        price.setAmount(amount);
        price.setCurrency(currency);
        price.setTimestamp(priceDateTime);
        priceRepository.save(price);
    }

    private Supermarket getSupermarket(String superuserName) {
        return supermarketCache.computeIfAbsent(superuserName, name ->
                supermarketRepository.findByName(name)
                        .orElseGet(() -> {
                            Supermarket newSupermarket = new Supermarket();
                            newSupermarket.setName(name);
                            newSupermarket.setPrices(new ArrayList<>());
                            return supermarketRepository.save(newSupermarket);
                        }));
    }


////////////////////Maybe I'll need this/////////////////////////////

    public void clearProcessingHistory() {
        processedFileRepository.deleteAll();
        log.info("Cleared processing history");
    }


    public boolean isFileProcessed(String fileName) {
        return processedFileRepository.findById(fileName.toLowerCase()).isPresent();
    }


    public List<ProcessedFile> getAllProcessedFiles() {
        return processedFileRepository.findAll();
    }

}