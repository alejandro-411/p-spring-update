package com.eam.p_spring_update.service;

import com.eam.p_spring_update.entity.Producto;
import com.eam.p_spring_update.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;


    // Obtener un producto por su código (con validación si no existe)
    public Producto obtenerPorCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el producto con el código proporcionado"));
    }


    // Actualizar producto por código (sin permitir cambiar el código)
    public Producto actualizarProducto(String codigo, Producto nuevoProducto) {
        Optional<Producto> productoExistente = productoRepository.findByCodigo(codigo);

        if (!codigo.equals(nuevoProducto.getCodigo())) {
            throw new IllegalArgumentException("No se permite modificar el código del producto");
        }

        if (productoExistente.isEmpty()) {
            throw new NoSuchElementException("No se encontró el producto con el código proporcionado");
        }

        Producto producto = productoExistente.get();
        producto.setNombre(nuevoProducto.getNombre());
        producto.setPrecio(nuevoProducto.getPrecio());
        producto.setCantidad(nuevoProducto.getCantidad());

        return productoRepository.save(producto);
    }

}
