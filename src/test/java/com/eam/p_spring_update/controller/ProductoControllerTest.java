package com.eam.p_spring_update.controller;

import com.eam.p_spring_update.entity.Producto;
import com.eam.p_spring_update.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void actualizarProducto_DeberiaActualizarYRetornarOk() throws Exception {
        Producto producto = new Producto(1L, "P001", "Camisa actualizada", 60000.0, 8);

        when(productoService.actualizarProducto(eq("P001"), any()))
                .thenReturn(producto);

        mockMvc.perform(put("/api/productos/actualizar/P001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.producto.nombre").value("Camisa actualizada"));

    }

    @Test
    void actualizarProducto_DeberiaRetornarBadRequestSiError() throws Exception {
        Producto producto = new Producto(1L, "P002", "Camisa", 50000.0, 10);

        when(productoService.actualizarProducto(eq("P001"), any()))
                .thenThrow(new IllegalArgumentException("No se permite modificar el código"));

        mockMvc.perform(put("/api/productos/actualizar/P001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(producto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se permite modificar el código"));
    }
}
