package com.stock.stock_control_backend.model;

import com.stock.stock_control_backend.model.enums.ProductTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProductTypeEnum type;

    private BigDecimal supplierValue;

    private Integer strockQuantity;
}
