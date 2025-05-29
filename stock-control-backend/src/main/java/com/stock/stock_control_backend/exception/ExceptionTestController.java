package com.stock.stock_control_backend.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Hidden
@Validated
@RestController
@RequestMapping("/test")
public class ExceptionTestController {

    @GetMapping("/entity-not-found")
    public void throwEntityNotFound() {
        throw new EntityNotFoundException("Entity not found");
    }

    @GetMapping("/product-exception")
    public void throwProductException() {
        throw new ProductException("Produto não encontrado");
    }

    @GetMapping("/stock-movement-exception")
    public void throwStockMovementException() {
        throw new StockMovementException("Movimentação inválida");
    }

    @GetMapping("/exception")
    public void throwGenericException() {
        throw new RuntimeException("Erro genérico");
    }

    @GetMapping("/validation-error")
    public void throwValidation(@RequestParam @NotBlank(message = "must not be blank") String description) {
        // dispara MethodArgumentNotValidException automaticamente
    }
}
