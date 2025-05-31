package com.stock.stock_control_backend.service;

import com.stock.stock_control_backend.config.StockMovementMapper;
import com.stock.stock_control_backend.dto.StockMovementDTO;
import com.stock.stock_control_backend.exception.StockMovementException;
import com.stock.stock_control_backend.model.ProductEntity;
import com.stock.stock_control_backend.model.StockMovementEntity;
import com.stock.stock_control_backend.model.enums.MovementTypeEnum;
import com.stock.stock_control_backend.repository.ProductRepository;
import com.stock.stock_control_backend.repository.StockMovementRepository;
import com.stock.stock_control_backend.utils.ApplicationMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;

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

        StockMovementEntity movimentoEntity = stockMovementMapper.toEntity(movimento);
        movimentoEntity.setProduct(produto);

        StockMovementEntity saved = movimentoRepository.save(movimentoEntity);

        return stockMovementMapper.toDto(saved);
    }

    public Page<StockMovementDTO> listarMovimentacoes(int page, int size) {
        NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return movimentoRepository.findAll(PageRequest.of(page, size))
                .map(mov -> {
                    StockMovementDTO dto = stockMovementMapper.toDto(mov);
                    if (dto.getSalePrice() != null) {
                        dto.setSalePriceFormatted(formatador.format(dto.getSalePrice()));
                    }
                    return dto;
                });
    }
}
