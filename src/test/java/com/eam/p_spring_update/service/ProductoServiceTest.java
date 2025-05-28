package com.eam.p_spring_update.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.NoSuchElementException;

import com.eam.p_spring_update.repository.ProductoRepository;
import com.eam.p_spring_update.entity.Producto;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;


    @Test
    void actualizarProducto_DeberiaActualizarCorrectamenteSiExisteYCodigoCoincide() {
        Producto existente = new Producto(1L, "P001", "Camisa", 50000.0, 10);
        Producto actualizado = new Producto(1L, "P001", "Camisa actualizada", 55000.0, 15);

        when(productoRepository.findByCodigo("P001")).thenReturn(Optional.of(existente));
        when(productoRepository.save(any())).thenReturn(actualizado);

        Producto resultado = productoService.actualizarProducto("P001", actualizado);

        assertNotNull(resultado);
        assertEquals("Camisa actualizada", resultado.getNombre());
        assertEquals(55000f, resultado.getPrecio());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void actualizarProducto_DeberiaLanzarExcepcionSiCodigoNoCoincide() {
        Producto actualizado = new Producto(null, "P999", "Error", 1000.0, 1);  // código modificado

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productoService.actualizarProducto("P001", actualizado);
        });

        assertEquals("No se permite modificar el código del producto", exception.getMessage());
        verify(productoRepository, never()).save(any());

    }

    @Test
    void actualizarProducto_DeberiaLanzarExcepcionSiProductoNoExiste() {
        Producto actualizado = new Producto(null, "P001", "Camisa actualizada", 55000.0, 15);

        when(productoRepository.findByCodigo("P001")).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            productoService.actualizarProducto("P001", actualizado);
        });

        assertEquals("No se encontró el producto con el código proporcionado", exception.getMessage());
        verify(productoRepository, never()).save(any());
    }


}
