package com.stock.stock_control_backend.repository;

import com.stock.stock_control_backend.model.ProductHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductHistoryRepository extends JpaRepository<ProductHistoryEntity, Long> {

    List<ProductHistoryEntity> findByProductId(Long productId);
}