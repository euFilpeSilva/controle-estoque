package com.stock.stock_control_backend.config;

import com.stock.stock_control_backend.dto.StockMovementDTO;
import com.stock.stock_control_backend.model.StockMovement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {

    @Mapping(target = "productId", source = "product.id")
     StockMovementDTO toDto(StockMovement stockMovement);

    @Mapping(target = "product.id", source = "productId")
     StockMovement toEntity(StockMovementDTO stockMovementDTO);

     List<StockMovementDTO> toDtoList(List<StockMovement> stockMovements);

     List<StockMovement> toEntityList(List<StockMovementDTO> stockMovementDTOs);

}
