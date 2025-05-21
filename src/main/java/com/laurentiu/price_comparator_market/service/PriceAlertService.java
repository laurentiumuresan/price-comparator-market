package com.laurentiu.price_comparator_market.service;

import com.laurentiu.price_comparator_market.dto.PriceAlertRequestDTO;
import com.laurentiu.price_comparator_market.entity.Price;
import com.laurentiu.price_comparator_market.entity.PriceAlert;
import com.laurentiu.price_comparator_market.entity.Product;
import com.laurentiu.price_comparator_market.repository.PriceAlertRepository;
import com.laurentiu.price_comparator_market.repository.PriceRepository;
import com.laurentiu.price_comparator_market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceAlertService {
    private final PriceRepository priceRepository;
    private final ProductRepository productRepository;
    private final PriceAlertRepository priceAlertRepository;
    private final EmailService emailService;

    public PriceAlert createPriceAlert(PriceAlertRequestDTO request) {

        Product product = productRepository.findByName(request.productName())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        PriceAlert alert = new PriceAlert();
        alert.setProduct(product);
        alert.setTargetPrice(request.targetPrice());
        alert.setUserEmail(request.userEmail());

        PriceAlert savedAlert = priceAlertRepository.save(alert);

        checkCurrentPrice(savedAlert);

        return savedAlert;
    }

    private void checkCurrentPrice(PriceAlert alert){
        List<Price> currentPrices = priceRepository.findLatestPriceByProductName(alert.getProduct().getName());

        currentPrices.stream()
                .min((p1, p2) -> p1.getAmount().compareTo(p2.getAmount()))
                .filter(lowestPrice -> lowestPrice.getAmount().compareTo(alert.getTargetPrice()) <=0)
                .ifPresent(lowestPrice -> {
                    log.info("Price alert triggered for product: " + alert.getProduct().getName() + " at " + lowestPrice.getTimestamp() + " with price: " + lowestPrice.getAmount());

                    emailService.sendPriceAlert(
                            alert.getUserEmail(),
                            alert.getProduct().getName(),
                            lowestPrice.getAmount(),
                            alert.getTargetPrice(),
                            lowestPrice.getSupermarket().getName());
                    log.info("Attempting to send email.");

                    alert.setActive(false);
                    priceAlertRepository.save(alert);
                });

    }

    @Scheduled(fixedRate = 600000)
    public void checkAllActiveAlerts(){
        List<PriceAlert> activeAlerts = priceAlertRepository.findByIsActiveTrue();
        activeAlerts.forEach(this::checkCurrentPrice);
     }

    public void deletePriceAlert(Long id){
        PriceAlert alert = priceAlertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Price alert not found"));
        priceAlertRepository.delete(alert);
       log.info("Price alert deleted for product: " + alert.getProduct().getName());
    }
}
