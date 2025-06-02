package com.stock.stock_control_backend.utils;

public enum ApplicationMessages {
    PRODUCT_NOT_FOUND("Produto com ID %d não encontrado"),
    MOVEMENT_NOT_FOUND("movimentação não encontrada"),
    INSUFFICIENT_STOCK("Quantidade insuficiente em estoque");

    private final String message;

    ApplicationMessages(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }

    public String getMessage() {
        return message;
    }
}
