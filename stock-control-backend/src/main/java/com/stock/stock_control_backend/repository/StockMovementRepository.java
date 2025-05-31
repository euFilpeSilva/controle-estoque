package com.stock.stock_control_backend.repository;

import com.stock.stock_control_backend.model.StockMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementRepository extends JpaRepository<StockMovementEntity, Long> {

}
