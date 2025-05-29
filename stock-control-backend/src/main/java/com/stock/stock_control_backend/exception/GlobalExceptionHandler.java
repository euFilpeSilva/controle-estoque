package com.stock.stock_control_backend.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String FIELDS = "fields";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        TIMESTAMP, LocalDateTime.now(),
                        STATUS, 500,
                        ERROR, "Internal Server Error",
                        MESSAGE, ex.getMessage()
                ));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        TIMESTAMP, LocalDateTime.now(),
                        STATUS, 404,
                        ERROR, "Not Found",
                        MESSAGE, ex.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (msg1, msg2) -> msg1
                ));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        TIMESTAMP, LocalDateTime.now(),
                        STATUS, 400,
                        ERROR, "Validation Error",
                        FIELDS, errors
                ));
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Map<String, Object>> handleProductException(ProductException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        TIMESTAMP, LocalDateTime.now(),
                        STATUS, 404,
                        ERROR, "Product Not Found",
                        MESSAGE, ex.getMessage()
                ));
    }

    @ExceptionHandler(StockMovementException.class)
    public ResponseEntity<Map<String, Object>> handleStockMovementException(StockMovementException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        TIMESTAMP, LocalDateTime.now(),
                        STATUS, 400,
                        ERROR, "Stock Movement Error",
                        MESSAGE, ex.getMessage()
                ));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParams(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        TIMESTAMP, LocalDateTime.now(),
                        STATUS, 400,
                        ERROR, "Missing Request Parameter",
                        MESSAGE, ex.getMessage()
                ));
    }
}