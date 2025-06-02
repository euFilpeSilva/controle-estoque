package com.stock.stock_control_backend.config;

import com.stock.stock_control_backend.dto.StockMovementDTO;
import com.stock.stock_control_backend.model.StockMovementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {

    default StockMovementDTO toDto(StockMovementEntity movimento) {
        StockMovementDTO dto = new StockMovementDTO();
        dto.setId(movimento.getId());
        dto.setProductId(movimento.getProduct().getId());
        dto.setMovementType(movimento.getMovementType());
        dto.setSalePrice(movimento.getSalePrice());
        dto.setSaleDate(movimento.getSaleDate());
        dto.setQuantityMovement(movimento.getQuantityMovement());
        dto.setProductDescription(movimento.getProduct().getDescription());
        dto.setProductCode(movimento.getProduct().getCode());
        if (movimento.getSaleDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dto.setSaleDateFormatted(movimento.getSaleDate().format(formatter));
        }
        return dto;
    }

    @Mapping(target = "product.id", source = "productId")
    StockMovementEntity toEntity(StockMovementDTO stockMovementDTO);

    List<StockMovementDTO> toDtoList(List<StockMovementEntity> stockMovements);

    List<StockMovementEntity> toEntityList(List<StockMovementDTO> stockMovementDTOs);

}
