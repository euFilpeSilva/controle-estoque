package com.stock.stock_control_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.stock_control_backend.config.ProductMapper;
import com.stock.stock_control_backend.dto.*;
import com.stock.stock_control_backend.exception.ProductException;
import com.stock.stock_control_backend.model.ProductEntity;
import com.stock.stock_control_backend.model.ProductHistoryEntity;
import com.stock.stock_control_backend.model.StockMovementEntity;
import com.stock.stock_control_backend.model.enums.MovementTypeEnum;
import com.stock.stock_control_backend.model.enums.ProductTypeEnum;
import com.stock.stock_control_backend.repository.ProductHistoryRepository;
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
import java.util.*;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductHistoryRepository historyRepository;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;

    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        ProductEntity entity = productMapper.toEntity(request);

        if (isNull(request.getCode()) || request.getCode().isBlank()) {
            String generatedCode = generateUniqueProductCode();
            entity.setCode(generatedCode);
        } else {
            if (repository.existsByCode(request.getCode())) {
                throw new IllegalArgumentException("Código de produto já existente: " + request.getCode());
            }
            entity.setCode(request.getCode());
        }

        ProductEntity saved = repository.save(entity);
        return productMapper.toDTO(saved);
    }

    private String generateUniqueProductCode() {
        String code;
        do {
            code = "PRD-" + String.format("%04d", new Random().nextInt(10000));
        } while (repository.existsByCode(code));
        return code;
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) throws JsonProcessingException {
        ProductEntity existing = repository.findById(id)
                .orElseThrow(() -> new ProductException("Produto não encontrado"));

        String previousState = objectMapper.writeValueAsString(existing);

        productMapper.updateEntityFromDto(dto, existing);
        ProductEntity updated = repository.save(existing);

        String updatedState = objectMapper.writeValueAsString(updated);

        ProductHistoryEntity history = new ProductHistoryEntity();
        history.setProductId(id);
        history.setPreviousState(previousState);
        history.setUpdatedState(updatedState);
        history.setChangeDate(LocalDateTime.now());
        historyRepository.save(history);

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
                    .mapToInt(StockMovementEntity::getQuantityMovement)
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

        return new ProfitProductDTO(product.getCode(), product.getDescription(), totalSaida, lucro, lucroFormatado);
    }

    public Page<ProfitProductDTO> getAllProfits(int page, int size, String startDate, String endDate) {
        List<ProductResponseDTO> products = getAllProducts(page, size).getContent();
        List<ProfitProductDTO> profits = products.stream()
                .map(p -> calculateProfit(p.getId(), startDate, endDate))
                .toList();
        return new PageImpl<>(profits, PageRequest.of(page, size), profits.size());
    }

    private Object formatSupplierAndSalePrice(Object stateObj) {
        if (stateObj instanceof Map<?, ?>) {
            Map<String, Object> map = (Map<String, Object>) stateObj;
            NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Object supplierValue = map.get("supplierValue");
            if (supplierValue instanceof Number) {
                map.put("supplierValueFormatted", formatador.format(supplierValue));
            }

            Object stockMovements = map.get("stockMovements");
            if (stockMovements instanceof List<?> list) {
                for (Object item : list) {
                    if (item instanceof Map<?, ?>) {
                        Map<String, Object> movement = (Map<String, Object>) item;
                        Object salePrice = movement.get("salePrice");
                        if (salePrice instanceof Number) {
                            movement.put("salePriceFormatted", formatador.format(salePrice));
                        }
                        Object saleDate = movement.get("saleDate");
                        if (saleDate instanceof String) {
                            try {
                                LocalDateTime dateTime = LocalDateTime.parse((String) saleDate);
                                movement.put("saleDateFormatted", dateTime.format(dateFormatter));
                            } catch (Exception ignored) {
                                movement.put("saleDateFormatted", saleDate);
                            }
                        }
                    }
                }
            }
            return map;
        }
        return stateObj;
    }

    public List<ProductHistoryDTO> getProductHistory(Long productId) {
        List<ProductHistoryEntity> entities = historyRepository.findByProductId(productId);
        List<ProductHistoryDTO> dtos = new ArrayList<>();
        for (ProductHistoryEntity entity : entities) {
            ProductHistoryDTO dto = new ProductHistoryDTO();
            dto.setId(entity.getId());
            dto.setProductId(entity.getProductId());
            try {
                Object prev = objectMapper.readValue(entity.getPreviousState(), Object.class);
                Object updated = objectMapper.readValue(entity.getUpdatedState(), Object.class);
                dto.setPreviousState(formatSupplierAndSalePrice(prev));
                dto.setUpdatedState(formatSupplierAndSalePrice(updated));
            } catch (JsonProcessingException e) {
                dto.setPreviousState(null);
                dto.setUpdatedState(null);
            }
            dto.setChangeDate(entity.getChangeDate());
            dtos.add(dto);
        }
        return dtos;
    }
}
