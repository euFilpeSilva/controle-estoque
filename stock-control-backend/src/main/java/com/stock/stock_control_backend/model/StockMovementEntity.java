package com.stock.stock_control_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.stock.stock_control_backend.model.enums.MovementTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private ProductEntity product;

    @Enumerated(EnumType.STRING)
    private MovementTypeEnum movementType;

    private BigDecimal salePrice;

    private LocalDateTime saleDate;

    private Integer quantityMovement;
}
