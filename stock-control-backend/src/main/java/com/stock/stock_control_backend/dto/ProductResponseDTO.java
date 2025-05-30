package com.stock.stock_control_backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductResponseDTO {

    private Long id;
    private String description;
    private String type;
    private BigDecimal supplierValue;
    private String supplierValueFormatted;
    private Integer stockQuantity;
    private List<StockMovementDTO> stockMovements = new ArrayList<>();
}
