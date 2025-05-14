package com.laurentiu.price_comparator_market.controller;

import com.laurentiu.price_comparator_market.service.CsvImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImportController {
    private final CsvImportService csvImportService;
    
    record ImportRequest(String directoryPath) {}
    
    @PostMapping("/import")
    public ResponseEntity<String> importData(@RequestBody ImportRequest request) {
        try {
            csvImportService.processDirectory(request.directoryPath());
            return ResponseEntity.ok("Import successful");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Import error: " + e.getMessage());
        }
    }
}