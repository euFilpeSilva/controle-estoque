package com.stock.stock_control_backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.stock.stock_control_backend.model.enums.ProductTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProductTypeEnum type;

    private BigDecimal supplierValue;

    private Integer stockQuantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<StockMovementEntity> stockMovements = new ArrayList<>();
}
