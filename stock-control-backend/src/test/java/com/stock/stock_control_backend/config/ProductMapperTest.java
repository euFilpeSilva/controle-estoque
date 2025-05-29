package com.stock.stock_control_backend.config;

import com.stock.stock_control_backend.dto.ProductRequestDTO;
import com.stock.stock_control_backend.dto.ProductResponseDTO;
import com.stock.stock_control_backend.dto.StockMovementDTO;
import com.stock.stock_control_backend.model.ProductEntity;
import com.stock.stock_control_backend.model.StockMovement;
import com.stock.stock_control_backend.model.enums.ProductTypeEnum;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapper() {
        @Override
        public ProductEntity toEntity(ProductRequestDTO dto) {
            return null;
        }

        @Override
        public ProductEntity toEntity(ProductResponseDTO dto) {
            return null;
        }

        @Override
        public ProductResponseDTO toDTO(ProductEntity entity) {
            return null;
        }

        @Override
        public List<ProductResponseDTO> toDTOList(List<ProductEntity> entities) {
            return List.of();
        }

        @Override
        public void updateEntityFromDto(ProductRequestDTO dto, ProductEntity entity) {

        }
    };

    @Test
    void mapStockMovements_deveRetornarListaVaziaSeNull() {
        List<StockMovementDTO> result = mapper.mapStockMovements(null, mock(StockMovementMapper.class));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void mapStockMovements_deveChamarMapperSeNaoNull() {
        List<StockMovement> movements = List.of(new StockMovement());
        StockMovementMapper stockMovementMapper = mock(StockMovementMapper.class);
        List<StockMovementDTO> dtos = List.of(new StockMovementDTO());
        when(stockMovementMapper.toDtoList(movements)).thenReturn(dtos);

        List<StockMovementDTO> result = mapper.mapStockMovements(movements, stockMovementMapper);

        assertEquals(dtos, result);
        verify(stockMovementMapper).toDtoList(movements);
    }

    @Test
    void mapStringToEnum_deveConverterStringParaEnum() {
        assertEquals(ProductTypeEnum.ELECTRONICS, mapper.mapStringToEnum("ELECTRONICS"));
    }
}