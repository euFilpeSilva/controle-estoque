package com.stock.stock_control_backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {

    private String description;
    private String type;
    private BigDecimal supplierValue;
    private Integer stockQuantity;
}
