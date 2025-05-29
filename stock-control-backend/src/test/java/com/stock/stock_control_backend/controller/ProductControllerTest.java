package com.stock.stock_control_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.stock_control_backend.dto.*;
import com.stock.stock_control_backend.model.enums.ProductTypeEnum;
import com.stock.stock_control_backend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarProdutoERetornarOk() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO();
        ProductResponseDTO response = new ProductResponseDTO();

        Mockito.when(productService.createProduct(any())).thenReturn(response);

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void deveRetornarProdutosPaginados() throws Exception {
        ProductResponseDTO dto1 = new ProductResponseDTO();
        ProductResponseDTO dto2 = new ProductResponseDTO();
        Page<ProductResponseDTO> page = new PageImpl<>(List.of(dto1, dto2), PageRequest.of(0, 10), 2);

        Mockito.when(productService.getAllProducts(0, 10)).thenReturn(page);

        mockMvc.perform(get("/api/product")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {
        ProductResponseDTO response = new ProductResponseDTO();

        Mockito.when(productService.getProduct(1L)).thenReturn(response);

        mockMvc.perform(get("/api/product/detalhar/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void deveDeletarProduto() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/product/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveAtualizarProduto() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO();
        ProductResponseDTO response = new ProductResponseDTO();

        Mockito.when(productService.updateProduct(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void deveConsultarPorTipo() throws Exception {
        ProductSummaryDTO summary = new ProductSummaryDTO("desc", 10, 2);
        List<ProductSummaryDTO> list = List.of(summary);

        Mockito.when(productService.searchByType(ProductTypeEnum.ELECTRONICS)).thenReturn(list);

        mockMvc.perform(get("/api/product/tipo/ELECTRONICS"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }

    @Test
    void deveCalculateProfit() throws Exception {
        ProfitProductDTO lucro = new ProfitProductDTO("desc", 3, BigDecimal.valueOf(10));

        Mockito.when(productService.calculateProfit(1L)).thenReturn(lucro);

        mockMvc.perform(get("/api/product/1/lucro"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(lucro)));
    }
}