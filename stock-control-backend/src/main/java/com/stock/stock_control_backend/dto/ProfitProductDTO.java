package com.stock.stock_control_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ProfitProductDTO {

    private String code;
    private String description;
    private int totalOutputs;
    private BigDecimal totalProfit;
    private String totalProfitFormatted;
}
