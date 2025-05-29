package com.stock.stock_control_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.stock_control_backend.dto.StockMovementDTO;
import com.stock.stock_control_backend.service.StockMovementService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockMovementController.class)
class StockMovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // uso correto no WebMvcTest
    private StockMovementService stockMovementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRegistrarMovimentoERetornarCreated() throws Exception {
        StockMovementDTO request = new StockMovementDTO();
        StockMovementDTO response = new StockMovementDTO();

        Mockito.when(stockMovementService.recordMovement(any(StockMovementDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
