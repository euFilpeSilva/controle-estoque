package com.stock.stock_control_backend.repository;

import com.stock.stock_control_backend.model.ProductEntity;
import com.stock.stock_control_backend.model.enums.ProductTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByType(ProductTypeEnum tipoProduto);

    boolean existsByCode(String code);
}
