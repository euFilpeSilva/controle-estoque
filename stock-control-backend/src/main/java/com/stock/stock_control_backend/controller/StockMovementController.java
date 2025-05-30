package com.stock.stock_control_backend.controller;

import com.stock.stock_control_backend.dto.StockMovementDTO;
import com.stock.stock_control_backend.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/movements")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovement;

    @PostMapping
    public ResponseEntity<StockMovementDTO> recordMovement(@RequestBody StockMovementDTO movimento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockMovement.recordMovement(movimento));
    }

    @GetMapping
    public ResponseEntity<Page<StockMovementDTO>> listarMovimentacoes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(stockMovement.listarMovimentacoes(page, size));
    }
}
