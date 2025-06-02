package com.stock.stock_control_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.stock_control_backend.config.ProductMapper;
import com.stock.stock_control_backend.dto.*;
import com.stock.stock_control_backend.exception.ProductException;
import com.stock.stock_control_backend.model.ProductEntity;
import com.stock.stock_control_backend.model.StockMovementEntity;
import com.stock.stock_control_backend.model.enums.MovementTypeEnum;
import com.stock.stock_control_backend.model.enums.ProductTypeEnum;
import com.stock.stock_control_backend.repository.ProductHistoryRepository;
import com.stock.stock_control_backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository repository;
    private ProductMapper productMapper;
    private ProductService service;

    @BeforeEach
    void setUp() {
        repository = mock(ProductRepository.class);
        ProductHistoryRepository historyRepository = mock(ProductHistoryRepository.class);
        productMapper = mock(ProductMapper.class);
        service = new ProductService(repository, historyRepository, productMapper, new ObjectMapper());
    }

    @Test
    void deveCriarProduto() {
        ProductRequestDTO request = new ProductRequestDTO();
        ProductEntity entity = new ProductEntity();
        ProductEntity saved = new ProductEntity();
        ProductResponseDTO response = new ProductResponseDTO();

        when(productMapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(productMapper.toDTO(saved)).thenReturn(response);

        ProductResponseDTO result = service.createProduct(request);

        assertEquals(response, result);
        verify(repository).save(entity);
    }

    @Test
    void deveLancarExcecaoAoAtualizarProdutoNaoEncontrado() {
        Long id = 1L;
        ProductRequestDTO dto = new ProductRequestDTO();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductException.class, () -> service.updateProduct(id, dto));
    }

    @Test
    void deveBuscarProdutoPorId() {
        Long id = 1L;
        ProductEntity entity = new ProductEntity();
        ProductResponseDTO response = new ProductResponseDTO();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(productMapper.toDTO(entity)).thenReturn(response);

        ProductResponseDTO result = service.getProduct(id);

        assertEquals(response, result);
    }

    @Test
    void deveLancarExcecaoAoBuscarProdutoNaoEncontrado() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ProductException.class, () -> service.getProduct(id));
    }

    @Test
    void deveRetornarTodosProdutosPaginados() {
        int page = 0, size = 2;
        List<ProductEntity> entities = Arrays.asList(new ProductEntity(), new ProductEntity());
        Page<ProductEntity> pageEntities = new PageImpl<>(entities, PageRequest.of(page, size), 2);
        List<ProductResponseDTO> dtoList = Arrays.asList(new ProductResponseDTO(), new ProductResponseDTO());

        when(repository.findAll(PageRequest.of(page, size))).thenReturn(pageEntities);
        when(productMapper.toDTOList(entities)).thenReturn(dtoList);

        Page<ProductResponseDTO> result = service.getAllProducts(page, size);

        assertEquals(2, result.getContent().size());
        assertEquals(dtoList, result.getContent());
    }

    @Test
    void deveDeletarProdutoExistente() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);

        service.deleteProduct(id);

        verify(repository).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoDeletarProdutoNaoExistente() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(ProductException.class, () -> service.deleteProduct(id));
        verify(repository, never()).deleteById(id);
    }

    @Test
    void deveSearchByType() {
        ProductTypeEnum tipo = ProductTypeEnum.ELECTRONICS;
        ProductEntity entity = new ProductEntity();
        entity.setDescription("desc");
        entity.setStockQuantity(10);

        StockMovementEntity mov1 = new StockMovementEntity();
        mov1.setMovementType(MovementTypeEnum.EXIT);
        mov1.setQuantityMovement(3);

        StockMovementEntity mov2 = new StockMovementEntity();
        mov2.setMovementType(MovementTypeEnum.ENTRY);
        mov2.setQuantityMovement(5);

        entity.setStockMovements(Arrays.asList(mov1, mov2));

        when(repository.findByType(tipo)).thenReturn(List.of(entity));

        List<ProductSummaryDTO> result = service.searchByType(tipo);

        assertEquals(1, result.size());
        assertEquals("desc", result.get(0).getDescription());
        assertEquals(10, result.get(0).getAvailableQuantity());
        assertEquals(3, result.get(0).getTotalOutputs());
    }

    @Test
    void deveCalculateProfit() {
        Long id = 1L;
        ProductResponseDTO product = new ProductResponseDTO();
        product.setDescription("desc");
        product.setSupplierValue(BigDecimal.valueOf(10));

        StockMovementDTO saida1 = new StockMovementDTO();
        saida1.setMovementType(MovementTypeEnum.EXIT);
        saida1.setQuantityMovement(2);
        saida1.setSalePrice(BigDecimal.valueOf(15));

        StockMovementDTO saida2 = new StockMovementDTO();
        saida2.setMovementType(MovementTypeEnum.EXIT);
        saida2.setQuantityMovement(1);
        saida2.setSalePrice(BigDecimal.valueOf(15));

        product.setStockMovements(Arrays.asList(saida1, saida2));

        when(repository.findById(id)).thenReturn(Optional.of(new ProductEntity()));
        when(productMapper.toDTO(any())).thenReturn(product);

        ProfitProductDTO result = service.calculateProfit(id, null, null);

        assertEquals("desc", result.getDescription());
        assertEquals(3, result.getTotalOutputs());
        assertEquals(BigDecimal.valueOf(15), result.getTotalProfit());
    }
}
