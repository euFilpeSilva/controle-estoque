package com.stock.stock_control_backend.exception;

public class StockMovementException extends RuntimeException{
    public StockMovementException(String message) {
        super(message);
    }
}
