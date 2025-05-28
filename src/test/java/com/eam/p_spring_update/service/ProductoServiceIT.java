package com.eam.p_spring_update.service;

import com.eam.p_spring_update.entity.Producto;
import com.eam.p_spring_update.repository.ProductoRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductoServiceIT {

    @Container
    static MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:10.6")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariadb::getJdbcUrl);
        registry.add("spring.datasource.username", mariadb::getUsername);
        registry.add("spring.datasource.password", mariadb::getPassword);
    }

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    private Producto producto;

    @BeforeEach
    void cleanDB() {
        productoRepository.deleteAll();
    }

     @BeforeEach
    void setUp() {
        producto = new Producto(null, "P001", "Camisa", 50000.0, 10);
    }

   


    @Test
    @Order(7)
    void actualizarProducto_DeberiaActualizarCorrectamente() {
        // Crear el producto directamente con el repositorio
        Producto productoInicial = new Producto(null, "A001", "Camisa", 50000.0, 10);
        productoRepository.save(productoInicial);

        Producto actualizado = new Producto(null, "A001", "Camisa actualizada", 60000.0, 15);
        Producto resultado = productoService.actualizarProducto("A001", actualizado);

        assertEquals("Camisa actualizada", resultado.getNombre());
        assertEquals(60000.0, resultado.getPrecio());
        assertEquals(15, resultado.getCantidad());
    }

    @Test
    @Order(8)
    void actualizarProducto_DeberiaFallarSiCodigoNoCoincide() {
        // Crear el producto directamente con el repositorio
        Producto productoInicial = new Producto(null, "A002", "Camisa", 50000.0, 10);
        productoRepository.save(productoInicial);

        Producto conCodigoCambiado = new Producto(null, "OTRO", "Modificado", 70000.0, 5);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            productoService.actualizarProducto("A002", conCodigoCambiado);
        });

        assertEquals("No se permite modificar el código del producto", ex.getMessage());
    }

    @Test
    @Order(9)
    void actualizarProducto_DeberiaFallarSiNoExiste() {
        Producto producto = new Producto(null, "NOHAY", "Nada", 10000.0, 1);

        Exception ex = assertThrows(NoSuchElementException.class, () -> {
            productoService.actualizarProducto("NOHAY", producto);
        });

        assertEquals("No se encontró el producto con el código proporcionado", ex.getMessage());
    }
}
