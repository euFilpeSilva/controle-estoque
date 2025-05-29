package com.stock.stock_control_backend.service;

import com.stock.stock_control_backend.config.StockMovementMapper;
import com.stock.stock_control_backend.dto.StockMovementDTO;
import com.stock.stock_control_backend.exception.StockMovementException;
import com.stock.stock_control_backend.model.ProductEntity;
import com.stock.stock_control_backend.model.StockMovement;
import com.stock.stock_control_backend.model.enums.MovementTypeEnum;
import com.stock.stock_control_backend.repository.ProductRepository;
import com.stock.stock_control_backend.repository.StockMovementRepository;
import com.stock.stock_control_backend.utils.ApplicationMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockMovementService {

    private final ProductRepository productRepository;
    private final StockMovementRepository movimentoRepository;
    private final StockMovementMapper stockMovementMapper;

    public StockMovementDTO recordMovement(StockMovementDTO movimento) {
        ProductEntity produto = productRepository.findById(movimento.getProductId())
                .orElseThrow(() -> new StockMovementException(ApplicationMessages.MOVEMENT_NOT_FOUND.toString()));

        if (movimento.getMovementType() == MovementTypeEnum.EXIT) {
            if (produto.getStockQuantity() < movimento.getQuantityMovement()) {
                throw new StockMovementException(ApplicationMessages.INSUFFICIENT_STOCK.toString());
            }
            produto.setStockQuantity(produto.getStockQuantity() - movimento.getQuantityMovement());
        } else {
            produto.setStockQuantity(produto.getStockQuantity() + movimento.getQuantityMovement());
        }

        productRepository.save(produto);

        StockMovement movimentoEntity = stockMovementMapper.toEntity(movimento);
        movimentoEntity.setProduct(produto);

        StockMovement saved = movimentoRepository.save(movimentoEntity);

        return stockMovementMapper.toDto(saved);
    }
}
