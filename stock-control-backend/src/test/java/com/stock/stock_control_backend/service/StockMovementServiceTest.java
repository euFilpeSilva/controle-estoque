package com.stock.stock_control_backend.service;

import com.stock.stock_control_backend.config.StockMovementMapper;
import com.stock.stock_control_backend.dto.StockMovementDTO;
import com.stock.stock_control_backend.exception.StockMovementException;
import com.stock.stock_control_backend.model.ProductEntity;
import com.stock.stock_control_backend.model.StockMovement;
import com.stock.stock_control_backend.model.enums.MovementTypeEnum;
import com.stock.stock_control_backend.repository.ProductRepository;
import com.stock.stock_control_backend.repository.StockMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class StockMovementServiceTest {

    private ProductRepository productRepository;
    private StockMovementRepository movimentoRepository;
    private StockMovementMapper stockMovementMapper;
    private StockMovementService service;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        movimentoRepository = mock(StockMovementRepository.class);
        stockMovementMapper = mock(StockMovementMapper.class);
        service = new StockMovementService(productRepository, movimentoRepository, stockMovementMapper);
    }

    @Test
    void deveRecordMovementEntrada() {
        StockMovementDTO dto = new StockMovementDTO();
        dto.setProductId(1L);
        dto.setMovementType(MovementTypeEnum.ENTRY);
        dto.setQuantityMovement(10);

        ProductEntity produto = new ProductEntity();
        produto.setId(1L);
        produto.setStockQuantity(5);

        StockMovement movimentoEntity = new StockMovement();
        StockMovement savedEntity = new StockMovement();
        StockMovementDTO retornoDTO = new StockMovementDTO();

        when(productRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(stockMovementMapper.toEntity(dto)).thenReturn(movimentoEntity);
        when(movimentoRepository.save(movimentoEntity)).thenReturn(savedEntity);
        when(stockMovementMapper.toDto(savedEntity)).thenReturn(retornoDTO);

        StockMovementDTO result = service.recordMovement(dto);

        assertEquals(retornoDTO, result);
        assertEquals(15, produto.getStockQuantity());
        verify(productRepository).save(produto);
    }

    @Test
    void deveRecordMovementSaida() {
        StockMovementDTO dto = new StockMovementDTO();
        dto.setProductId(1L);
        dto.setMovementType(MovementTypeEnum.EXIT);
        dto.setQuantityMovement(3);

        ProductEntity produto = new ProductEntity();
        produto.setId(1L);
        produto.setStockQuantity(10);

        StockMovement movimentoEntity = new StockMovement();
        StockMovement savedEntity = new StockMovement();
        StockMovementDTO retornoDTO = new StockMovementDTO();

        when(productRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(stockMovementMapper.toEntity(dto)).thenReturn(movimentoEntity);
        when(movimentoRepository.save(movimentoEntity)).thenReturn(savedEntity);
        when(stockMovementMapper.toDto(savedEntity)).thenReturn(retornoDTO);

        StockMovementDTO result = service.recordMovement(dto);

        assertEquals(retornoDTO, result);
        assertEquals(7, produto.getStockQuantity());
        verify(productRepository).save(produto);
    }

    @Test
    void deveLancarExcecaoEstoqueInsuficiente() {
        StockMovementDTO dto = new StockMovementDTO();
        dto.setProductId(1L);
        dto.setMovementType(MovementTypeEnum.EXIT);
        dto.setQuantityMovement(20);

        ProductEntity produto = new ProductEntity();
        produto.setId(1L);
        produto.setStockQuantity(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(produto));

        assertThrows(StockMovementException.class, () -> service.recordMovement(dto));
        verify(productRepository, never()).save(any());
        verify(movimentoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoProdutoNaoEncontrado() {
        StockMovementDTO dto = new StockMovementDTO();
        dto.setProductId(99L);

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StockMovementException.class, () -> service.recordMovement(dto));
        verify(productRepository, never()).save(any());
        verify(movimentoRepository, never()).save(any());
    }
}