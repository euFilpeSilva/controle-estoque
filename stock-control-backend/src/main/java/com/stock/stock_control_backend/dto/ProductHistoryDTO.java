package com.stock.stock_control_backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductHistoryDTO {
    private Long id;
    private Long productId;
    private Object previousState;
    private Object updatedState;
    private LocalDateTime changeDate;
}