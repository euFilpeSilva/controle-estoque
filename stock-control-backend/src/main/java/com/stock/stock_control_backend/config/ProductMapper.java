package com.stock.stock_control_backend.config;

import com.stock.stock_control_backend.dto.ProductResponseDTO;
import com.stock.stock_control_backend.dto.ProductRequestDTO;
import com.stock.stock_control_backend.dto.StockMovementDTO;
import com.stock.stock_control_backend.model.ProductEntity;
import com.stock.stock_control_backend.model.StockMovementEntity;
import com.stock.stock_control_backend.model.enums.ProductTypeEnum;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    default List<StockMovementDTO> mapStockMovements(List<StockMovementEntity> movements, StockMovementMapper stockMovementMapper) {
        return movements == null ? new ArrayList<>() : stockMovementMapper.toDtoList(movements);
    }

    @Mapping(target = "type", expression = "java(mapStringToEnum(dto.getType()))")
    ProductEntity toEntity(ProductRequestDTO dto);

    ProductEntity toEntity(ProductResponseDTO dto);

    @Mapping(target = "type", expression = "java(entity.getType().name())")
    ProductResponseDTO toDTO(ProductEntity entity);

    List<ProductResponseDTO> toDTOList(List<ProductEntity> entities);

    default ProductTypeEnum mapStringToEnum(String value) {
        return ProductTypeEnum.valueOf(value);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProductRequestDTO dto, @MappingTarget ProductEntity entity);
}
