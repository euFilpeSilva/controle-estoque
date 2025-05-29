package com.stock.stock_control_backend.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExceptionTestController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRetornar404ParaEntityNotFoundException() throws Exception {
        mockMvc.perform(get("/test/entity-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveRetornar404ParaProductException() throws Exception {
        mockMvc.perform(get("/test/product-exception"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product Not Found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveRetornar400ParaStockMovementException() throws Exception {
        mockMvc.perform(get("/test/stock-movement-exception"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Stock Movement Error"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void deveRetornar500ParaExceptionGenerica() throws Exception {
        mockMvc.perform(get("/test/exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    void deveRetornar400ParaValidacaoComErro() throws Exception {
        mockMvc.perform(get("/test/validation-error"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing Request Parameter"))
                .andExpect(jsonPath("$.message").value("Required request parameter 'description' for method parameter type String is not present"));

    }
}
