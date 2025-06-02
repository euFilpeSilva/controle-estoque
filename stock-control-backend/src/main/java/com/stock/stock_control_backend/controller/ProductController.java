package com.stock.stock_control_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stock.stock_control_backend.dto.*;
import com.stock.stock_control_backend.model.enums.ProductTypeEnum;
import com.stock.stock_control_backend.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@Tag(name = "Product Management", description = "Endpoints for managing stock products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO request) {
        ProductResponseDTO dto = productService.createProduct(request);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.getAllProducts(page, size));
    }


    @GetMapping("/detalhar/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO productResponseDTO = productService.getProduct(id);
        return ResponseEntity.ok(productResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO dto) throws JsonProcessingException {
        ProductResponseDTO updatedProduct = productService.updateProduct(id, dto);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ProductSummaryDTO>> consultarPorTipo(@PathVariable ProductTypeEnum tipo) {
        return ResponseEntity.ok(productService.searchByType(tipo));
    }

    @GetMapping("/lucro")
    public ResponseEntity<Page<ProfitProductDTO>> getAllProfits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(productService.getAllProfits(page, size, startDate, endDate));
    }

    @GetMapping("/{id}/lucro")
    public ResponseEntity<ProfitProductDTO> getProfit(
            @PathVariable Long id,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(productService.calculateProfit(id, startDate, endDate));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ProductHistoryDTO>> getProductHistory(@PathVariable Long id) {
        List<ProductHistoryDTO> history = productService.getProductHistory(id);
        return ResponseEntity.ok(history);
    }
}
