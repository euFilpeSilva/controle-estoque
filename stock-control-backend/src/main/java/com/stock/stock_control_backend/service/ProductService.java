package com.stock.stock_control_backend.service;

import com.stock.stock_control_backend.config.ProductMapper;
import com.stock.stock_control_backend.dto.*;
import com.stock.stock_control_backend.exception.ProductException;
import com.stock.stock_control_backend.model.ProductEntity;
import com.stock.stock_control_backend.model.StockMovement;
import com.stock.stock_control_backend.model.enums.MovementTypeEnum;
import com.stock.stock_control_backend.model.enums.ProductTypeEnum;
import com.stock.stock_control_backend.repository.ProductRepository;
import com.stock.stock_control_backend.utils.ApplicationMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper productMapper;

    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        ProductEntity entity = productMapper.toEntity(request);
        ProductEntity saved = repository.save(entity);
        return productMapper.toDTO(saved);
    }


    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        ProductEntity existing = repository.findById(id)
                .orElseThrow(() -> new ProductException(ApplicationMessages.PRODUCT_NOT_FOUND.format(id)));

        productMapper.updateEntityFromDto(dto, existing);

        ProductEntity updated = repository.save(existing);
        return productMapper.toDTO(updated);
    }


    public ProductResponseDTO getProduct(Long id) {
        ProductEntity entity = repository.findById(id)
                .orElseThrow(() -> new ProductException(ApplicationMessages.PRODUCT_NOT_FOUND.format(id)));

        return productMapper.toDTO(entity);
    }


    public Page<ProductResponseDTO> getAllProducts(int page, int size) {
        Page<ProductEntity> entities = repository.findAll(PageRequest.of(page, size));

        List<ProductResponseDTO> dtoList = productMapper.toDTOList(entities.getContent());

        return new PageImpl<>(dtoList, entities.getPageable(), entities.getTotalElements());
    }


    public void deleteProduct(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ProductException(ApplicationMessages.PRODUCT_NOT_FOUND.format(id));
        }
    }

    public List<ProductSummaryDTO> consultarPorTipo(ProductTypeEnum tipo) {
        List<ProductSummaryDTO> list = new ArrayList<>();
        for (ProductEntity productEntity : repository.findByType(tipo)) {
            int totalSaidas = productEntity.getStockMovements().stream()
                    .filter(m -> m.getMovementTypeEnum() == MovementTypeEnum.EXIT)
                    .mapToInt(StockMovement::getQuantityMovement)
                    .sum();

            ProductSummaryDTO apply = new ProductSummaryDTO(productEntity.getDescription(), productEntity.getStockQuantity(), totalSaidas);
            list.add(apply);
        }
        return list;
    }

    public ProfitProductDTO calcularLucro(Long id) {
        ProductResponseDTO product = getProduct(id);

        List<StockMovementDTO> saidas = product.getStockMovements().stream()
                .filter(m -> m.getMovementTypeEnum() == MovementTypeEnum.EXIT)
                .toList();

        int totalSaida = saidas.stream().mapToInt(StockMovementDTO::getQuantityMovement).sum();

        BigDecimal lucro = saidas.stream()
                .map(m -> m.getSalePrice().subtract(product.getSupplierValue()).multiply(BigDecimal.valueOf(m.getQuantityMovement())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ProfitProductDTO(product.getDescription(), totalSaida, lucro);
    }
}
