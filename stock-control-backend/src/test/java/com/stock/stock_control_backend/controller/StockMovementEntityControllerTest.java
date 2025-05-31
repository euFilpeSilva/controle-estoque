package com.stock.stock_control_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.stock_control_backend.dto.StockMovementDTO;
import com.stock.stock_control_backend.service.StockMovementService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockMovementController.class)
class StockMovementEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockMovementService stockMovementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRegistrarMovimentoERetornarCreated() throws Exception {
        StockMovementDTO request = new StockMovementDTO();
        StockMovementDTO response = new StockMovementDTO();

        Mockito.when(stockMovementService.recordMovement(any(StockMovementDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void deveListarMovimentacoesComPaginacao() throws Exception {
        StockMovementDTO dto = new StockMovementDTO();
        Page<StockMovementDTO> page = new PageImpl<>(Collections.singletonList(dto), PageRequest.of(0, 10), 1);

        Mockito.when(stockMovementService.listarMovimentacoes(eq(0), eq(10)))
                .thenReturn(page);

        mockMvc.perform(get("/api/movements")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0]").exists());
    }
}
