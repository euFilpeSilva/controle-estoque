package com.stock.stock_control_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductSummaryDTO {

    private String description;
    private int availableQuantity;
    private int totalOutputs;
}
