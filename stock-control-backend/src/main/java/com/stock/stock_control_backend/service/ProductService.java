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
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        List<ProductResponseDTO> dtoList = productMapper.toDTOList(entities.getContent());
        dtoList.forEach(dto -> {
            if (dto.getSupplierValue() != null) {
                dto.setSupplierValueFormatted(formatador.format(dto.getSupplierValue()));
            }
        });

        return new PageImpl<>(dtoList, entities.getPageable(), entities.getTotalElements());
    }

    // ProductService.java
    public List<ProductResponseDTO> getAllProducts() {
        return repository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }


    public void deleteProduct(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ProductException(ApplicationMessages.PRODUCT_NOT_FOUND.format(id));
        }
    }

    public List<ProductSummaryDTO> searchByType(ProductTypeEnum tipo) {
        List<ProductSummaryDTO> list = new ArrayList<>();
        for (ProductEntity productEntity : repository.findByType(tipo)) {
            int totalSaidas = productEntity.getStockMovements().stream()
                    .filter(m -> m.getMovementType() == MovementTypeEnum.EXIT)
                    .mapToInt(StockMovement::getQuantityMovement)
                    .sum();

            ProductSummaryDTO apply = new ProductSummaryDTO(productEntity.getDescription(), productEntity.getStockQuantity(), totalSaidas);
            list.add(apply);
        }
        return list;
    }

    public ProfitProductDTO calculateProfit(Long id, String startDate, String endDate) {
        ProductResponseDTO product = getProduct(id);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate, formatter) : null;
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate, formatter) : null;

        List<StockMovementDTO> saidas = product.getStockMovements().stream()
                .filter(m -> m.getMovementType() == MovementTypeEnum.EXIT)
                .filter(m -> {
                    if (start != null && m.getSaleDate().isBefore(start)) return false;
                    if (end != null && m.getSaleDate().isAfter(end)) return false;
                    return true;
                })
                .toList();

        int totalSaida = saidas.stream().mapToInt(StockMovementDTO::getQuantityMovement).sum();

        BigDecimal lucro = saidas.stream()
                .map(m -> m.getSalePrice().subtract(product.getSupplierValue()).multiply(BigDecimal.valueOf(m.getQuantityMovement())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String lucroFormatado = formatador.format(lucro);

        return new ProfitProductDTO(product.getDescription(), totalSaida, lucro, lucroFormatado);
    }

    // ProductService.java

    public Page<ProfitProductDTO> getAllProfits(int page, int size, String startDate, String endDate) {
        List<ProductResponseDTO> products = getAllProducts(page, size).getContent();
        List<ProfitProductDTO> profits = products.stream()
                .map(p -> calculateProfit(p.getId(), startDate, endDate))
                .toList();
        return new PageImpl<>(profits, PageRequest.of(page, size), profits.size());
    }
}
