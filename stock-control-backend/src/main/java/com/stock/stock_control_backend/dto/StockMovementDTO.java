package com.stock.stock_control_backend.dto;

import com.stock.stock_control_backend.model.enums.MovementTypeEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class StockMovementDTO {
    private Long id;
    private Long productId;
    private MovementTypeEnum movementTypeEnum;
    private BigDecimal salePrice;
    private LocalDateTime saleDate;
    private Integer quantityMovement;
}
