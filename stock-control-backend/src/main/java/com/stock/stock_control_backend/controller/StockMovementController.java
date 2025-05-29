package com.stock.stock_control_backend.controller;

import com.stock.stock_control_backend.dto.StockMovementDTO;
import com.stock.stock_control_backend.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movements")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovement;

    @PostMapping
    public ResponseEntity<StockMovementDTO> recordMovement(@RequestBody StockMovementDTO movimento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockMovement.recordMovement(movimento));
    }
}
